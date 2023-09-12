
import PollSurface from "../poll/PollSurface"
import {useState, useEffect} from "react"
import {useNavigate} from "react-router-dom";
import "../../css/Dashboard.css"


export default function Dashboard() {
    const [pollSurfaces, setPollSurfaces] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        async function getAllPolls() {
            const response = await fetch(`https://${process.env.REACT_APP_LOAD_BALANCER_DNS}/api/getAllPolls`, {
                method: "GET",
                body: JSON.stringify(),
                headers: {
                    "Authorization": localStorage.getItem("token")
                }
            }).then(res => res.json());

            setPollSurfaces(response)
        }

        // getAllPolls().then(result => setPollSurfaces(result));
        getAllPolls()

    }, []);

    let isThereNoPolls = Array.isArray(pollSurfaces) && !pollSurfaces.length

    return (
        <>
            <h1 className="title">Dashboard</h1>
            <div className="dashboard-container">

                <button className="createPollBtn" onClick={() => navigate('/createPoll')}>Create Poll</button>
                <div className="allPolls-container">
                    {
                        !isThereNoPolls && pollSurfaces && pollSurfaces.map((pollSurface, index) => (
                            <PollSurface
                                key={index}
                                nameOfPoster={pollSurface.nameOfPoster}
                                pollContent={pollSurface.pollContent}
                                totalVotes={pollSurface.totalVotes}
                                pollId = {pollSurface.pollId}
                                hasPollEnded = {pollSurface.pollEnded}
                            />
                        ))
                    }
                </div>

            </div>
        </>

    );
}