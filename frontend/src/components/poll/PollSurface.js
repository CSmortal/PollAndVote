import {Fragment} from "react"
import {Navigate, useNavigate} from "react-router-dom"
import "../../css/PollSurface.css"

export default function PollSurface(props) {

    const { pollContent, totalVotes, nameOfPoster, pollId, hasPollEnded } = props
    const navigate = useNavigate()

    function handleClick() {
        navigate(`/viewPoll/${pollId}`, { state: { pollContent, nameOfPoster }})
    }

    return (
        <div className={hasPollEnded ? "pollSurfaceEnded" : "pollSurfaceOngoing" } onClick={handleClick}>
            <h2 className="pollSurface-content">{pollContent}</h2>
            <p className="pollSurface-totalVotes">{`Votes: ${totalVotes}`}</p>
            { !hasPollEnded && <p className="ongoing-status">Ongoing</p>}
            <p className="pollSurface-nameOfPoster">{nameOfPoster}</p>
        </div>

    )
}