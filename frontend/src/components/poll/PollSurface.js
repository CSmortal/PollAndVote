import {Fragment} from "react"
import {Navigate, useNavigate} from "react-router-dom"


export default function PollSurface(props) {

    const { pollContent, totalVotes, nameOfPoster, pollId } = props
    const navigate = useNavigate()

    function handleClick() {
        navigate(`/viewPoll/${pollId}`, { state: { pollContent, nameOfPoster }})
    }

    return (
        <Fragment>
            <div onClick={handleClick}>
                <p>{pollContent}</p>
                <p>{`Total Votes: ${totalVotes}`}</p>
                <p>{nameOfPoster}</p>
            </div>
        </Fragment>
    )
}