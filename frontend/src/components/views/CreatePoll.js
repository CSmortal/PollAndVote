import {useRef, useState} from "react";
import {Navigate, useNavigate} from "react-router-dom";
import CreatePollOption from "../poll/CreatePollOption";
import "../../css/CreatePoll.css"

export default function CreatePoll() {

    const [ pollContent, setPollContent ] = useState("")
    const [ onlyOneSelection, setOnlyOneSelection ] = useState(true)
    const [ options, setOptions ] = useState(['', '']) // polls can have minimum of 2 options
    const childRefs = useRef([])
    const navigate = useNavigate()

    function addOption() {
        if (options.length < 10) {
            setOptions([...options, ''])
        }
    }

    async function handleSubmitForm() {
        // get all the content from the CreatePollOption children
        const optionsContent = childRefs.current.map((childRef) => childRef.value)
        const filteredPollOptionDtos = []
        // after retrieving the content for each option, filter out those with no content
        for (let content of optionsContent) { // for of loop is to iterate over arrays, for in loop is to iterate over object fields
            if (content.length > 50) {
                // display a popup error from the bottom
                return;
            }
            const trimmedContent = content.trim()

            if (trimmedContent.length > 0) {
                filteredPollOptionDtos.push({ pollOptionContent: trimmedContent })
            }
        }

        if (filteredPollOptionDtos.length < 2) {
            // we need to visibly show the user later to add at least 2 options with content inside
            console.error("There needs to be at least 2 non-empty options for a poll")
            return;
        }

        if (pollContent.length < 3 || pollContent.length > 150) {
            console.error("Poll title should be between 3 and 150 characters long")
            return
        }

        // submit the content
        let response = await fetch(`https://pollandvotelb.csmortal.store/api/addPoll`, {
            method: "POST",
            body: JSON.stringify(
                {
                    pollDto: {
                        userId: localStorage.getItem("userId"),
                        pollContent: pollContent,
                        onlyOneSelection: onlyOneSelection
                    },

                    pollOptionDtoList: filteredPollOptionDtos
                }
            ),
            headers: {
                "Authorization" : localStorage.getItem("token"),
                "Content-type": "application/json" // this is EXTREMELY IMPORTANT
            }
        })
        response = await response.json();

        if (response) {
            console.log("New poll successfully created")
            navigate("/dashboard")
        } else {
            console.error("Couldn't create poll")
            console.log("Response error: " + JSON.stringify(response))
        }
    }

    function handleOneSelectionChange() {
        // console.log(onlyOneSelection)
        setOnlyOneSelection(!onlyOneSelection)
    }

    return (
        <div className="createPoll-container">

            <div className="createPoll-titleContainer">
                <input
                    className="createPoll-newPollTitle"
                    type="text"
                    value={pollContent}
                    onChange={e => setPollContent(e.target.value)}
                    placeholder="Enter your poll title..."
                />
                <p className="newPollTitle-instruction">Title must be between 3 to 150 characters</p>
            </div>


            <div className="createPoll-onlyAllowOneOption">
                <label className="onlyAllowOneOption-label" htmlFor="onlyOneSelection">Only allow one option to be selected</label>
                <input type="radio" name="onlyOneSelection" checked={onlyOneSelection} onClick={handleOneSelectionChange}/>
            </div>


            <div className="createPoll-pollOptionsContainer">

                { options.map((option, index) => (
                    // the refToComponent argument is a reference to this specific CreatePoll component, that is created when this specific CreatePoll is rendered
                    <CreatePollOption
                        key={index}
                        ref={(refToComponent) => childRefs.current[index] = refToComponent}
                    />))

                }
                <p className="createPoll-pollOptions-instruction">All options (must be 2 to 10 valid ones) must be between 1 to 50 characters</p>

                <div className="createPoll-addOptionAndSubmit-container">
                    { options.length < 10 && <button className="createPoll-addOptionBtn" onClick={addOption}>Add Option</button>}
                    <button className="submitNewPollBtn" onClick={handleSubmitForm}>Submit new poll</button>
                </div>

            </div>


        </div>
    )
}