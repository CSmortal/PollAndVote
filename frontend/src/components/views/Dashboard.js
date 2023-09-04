
import PollSurface from "../poll/PollSurface"
import {useState, useEffect} from "react"
import {useNavigate} from "react-router-dom";

export default function Dashboard() {
    const [pollSurfaces, setPollSurfaces] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        async function getAllPolls() {
            const response = await fetch("http://localhost:8080/api/getAllPolls", {
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
        <div>
            <h1>Dashboard</h1>
            <button className="createPollBtn" onClick={() => navigate('/createPoll')}>Create Poll</button>
            {
                !isThereNoPolls && pollSurfaces && pollSurfaces.map((pollSurface, index) => (
                    <PollSurface
                        key={index}
                        nameOfPoster={pollSurface.nameOfPoster}
                        pollContent={pollSurface.pollContent}
                        totalVotes={pollSurface.totalVotes}
                        pollId = {pollSurface.pollId}
                    />
                ))
            }
        </div>


    );
}