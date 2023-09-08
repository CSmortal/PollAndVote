import {useEffect, useState, } from "react"
import {useLocation, useNavigate, useParams} from "react-router-dom"
import PartialPollOption from "./PartialPollOption"
import FullPollOption from "./FullPollOption"
import "../../css/PollDetail.css"

export default function PollDetail() {
    const { pollId } = useParams();
    const { state } = useLocation();
    const navigate = useNavigate()

    const [ pollDetail, setPollDetail ] = useState({})
    const [ selectedOptions, setSelectedOptions ] = useState(new Set())

    const { mapOptionContentToPercentage, mapOptionIdToOptionContent, totalVotes, userIdOfPollCreator,
            hasLimitedView, hasUserVoted, voteOnlyForOneOption, mapOptionIdToVoteStatus } = pollDetail
    const { pollContent, nameOfPoster } = state;
    const userId = localStorage.getItem("userId")


    useEffect(() => {
        async function getPollInDetail() {
            return await fetch(`http://${process.env.REACT_APP_LOAD_BALANCER_DNS}/api/getPoll?pollId=${pollId}&userId=${userId}`, {
                headers: {
                    "Authorization" : localStorage.getItem("token")
                }
            })
                .then(res => res.json())
        }

        getPollInDetail()
            .then(res => {
                const selectedOptionsSet = new Set()

                if (res && res.mapOptionIdToVoteStatus) {
                    for (const [key, value] of Object.entries(res.mapOptionIdToVoteStatus)) {
                        // Object.entries returns an array, we need to use for-of instead for-in
                        selectedOptionsSet.add(key)
                    }
                    setSelectedOptions(selectedOptionsSet)
                    setPollDetail(res)
                }
            })
            .catch(err => console.error(`Failed to fetch PollDetail of pollId ${pollId}`))



    }, []);

    function handleOptionSelect(selectedOptionId) {
        // if the poll only accepts one option for voting, then if theres already one selected option, then we have to change the selected option.
        if (hasUserVoted) {
            return;
        }

        // if user has already selected this option, then we unselect it
        if (selectedOptions.has(selectedOptionId)) {
            const updatedSet = new Set(selectedOptions)
            updatedSet.delete(selectedOptionId)
            setSelectedOptions(updatedSet)
        } else if (voteOnlyForOneOption && selectedOptions.size > 0) {
            const newSet = new Set()
            newSet.add(selectedOptionId)
            setSelectedOptions(newSet)
        } else {
            // user has not voted, and either selectedOptions is empty, or can vote multiple options
            const updatedSet = new Set(selectedOptions)
            updatedSet.add(selectedOptionId)
            setSelectedOptions(updatedSet)
        }
    }

    async function handleSubmitVote() {
        if (selectedOptions.size === 0) {
            console.error("Please select an option(s) to vote for!") // later on we want to show this visibly to user
            return;
        }

        if (!hasUserVoted) {
            const response = await fetch(`http://${process.env.REACT_APP_LOAD_BALANCER_DNS}/api/vote`, {
                method: "POST",
                headers: {
                    "Authorization" : localStorage.getItem("token"),
                    "Content-type": "application/json" // this is EXTREMELY IMPORTANT
                },
                body: JSON.stringify({
                    pollId: pollId,
                    userIdOfVoter: userId,
                    optionIdToVoteList: [...selectedOptions]
                })
            })
                .then(res => res.json())
                .catch(err => console.error(err))
            // using the response we see if the submission was successful
            // if it was successful, then we might want to render something else
            if (response) {
                // we want to call the effect again and re-render
                setPollDetail({
                    ...pollDetail,
                    hasUserVoted: !hasUserVoted,
                    totalVotes: totalVotes + 1
                })
            }
        }


    }


    // If limited view and user hasnt voted, then can only see polling options
    // If limited view and user has voted, then can see options and voted options (cannot revote for ease)
    function handleBackButton() {
        navigate("/dashboard", { replace: true })
    }

    async function handleEndPoll() {
        const response = await fetch(`http://${process.env.REACT_APP_LOAD_BALANCER_DNS}/api/endPoll/${pollId}`, {
            method: "PUT",
            headers: {
                "Authorization" : localStorage.getItem("token")
            }
        }).then(res => res.json())

        if (response) {
            // then we want this component to be rerendered to see the full results of the poll
            // make another api call to getPollResults to get the full result
            const finalPollResult = await fetch(`http://${process.env.REACT_APP_LOAD_BALANCER_DNS}/api/getPoll?pollId=${pollId}&userId=${userId}`, {
                headers: {
                    "Authorization" : localStorage.getItem("token")
                }
            }).then(res => res.json())

            if (finalPollResult) {
                setPollDetail(finalPollResult) // causes final results to render
            } else {
                console.error("Successfully ended poll but failed to retrieve final poll results")
            }

        } else {
            console.error("Failed to end poll")
        }
    }

    // If non limited view, can see everything
    if (pollContent) {

        if (hasLimitedView) {
            return (
                <div className="pollDetail-container">
                    <button className="backToDashboard-btn" onClick={handleBackButton}>Back to Dashboard</button>

                    <div className="stickyContainerForTop">
                        <h2 className="title">{pollContent}</h2>
                        { userIdOfPollCreator === parseInt(userId) && <button className="endPoll-btn" onClick={handleEndPoll}>End poll</button> }
                        <h2 className="votes">{`Votes: ${totalVotes}`}</h2>
                    </div>

                    { !voteOnlyForOneOption && <h3 className="multipleSelections">Multiple selections allowed</h3> }

                    <div className="pollDetail-pollOptionsContainer">
                        { mapOptionIdToOptionContent &&
                            Object.entries(mapOptionIdToOptionContent).map(([optionId, optionContent], index) => (
                                <PartialPollOption
                                    key={index}
                                    optionContent={optionContent}
                                    hasUserVoted={hasUserVoted}
                                    onOptionSelect={id => handleOptionSelect(id)}
                                    optionId={optionId}
                                    isSelectedByUser={selectedOptions.has(optionId)}
                                    onlyOneOptionAllowed={voteOnlyForOneOption}
                                />
                            ) )
                        }
                        {!hasUserVoted && <button className="submitVote-btn" onClick={handleSubmitVote}>Submit Vote</button>}
                    </div>


                </div>




            )
        } else {
            return (
                <div className="pollDetail-container">
                    <button className="backToDashboard-btn" onClick={handleBackButton}>Back to Dashboard</button>

                    <div className="stickyContainerForTop">
                        <h2 className="title">{pollContent}</h2>
                        <h2 className="votes">{"Votes: " + totalVotes}</h2>)
                    </div>

                    { !voteOnlyForOneOption && <h3 className="multipleSelections">Multiple selections allowed</h3>}

                    <p className="pollEnded-announcement">Poll has ended, and here are the results!</p>

                    <div className="pollDetail-pollOptionsContainer">
                        { mapOptionIdToOptionContent &&
                            Object.entries(mapOptionIdToOptionContent).map(([optionId, optionContent], index) => (
                                <FullPollOption
                                    key={index}
                                    optionContent={optionContent}
                                    percentageVoted={mapOptionContentToPercentage[optionContent]}
                                    isSelectedByUser={selectedOptions.has(optionId)}
                                />
                            ) )
                        }

                    </div>

                </div>
            )
        }
    } else {
        return (<div></div>)
    }

}