import {Fragment, useContext, useState} from "react";
import {Link} from "react-router-dom";
import {AuthContext} from "../../App";
import "../../css/Login.css"

export default function Login() {
    const { setIsAuth } = useContext(AuthContext)
    const [ isRegistered, setIsRegistered ] = useState(true)
    const [inputs, setInputs] = useState({
        email: "",
        password: ""
    })

    const { email, password } = inputs;

    const onSubmitForm = async (e) => {
        e.preventDefault()

        const body = { email, password }

        try {
            const response = await fetch(`https://pollandvotelb.csmortal.store/auth/login`
        , {
                method: "POST",
                headers: {
                    "Content-type": "application/json"
                },
                body: JSON.stringify(body)
            }).then(res => res.json())

            if (response && response.success) {
                localStorage.setItem("token", response.token)
                localStorage.setItem("userId", response.userId)
                setIsAuth(true) // responsible for routing to the next route from login screen
            } else {
                setIsRegistered(false)
            }
        } catch (error) {
            setIsRegistered(false)
        }
    }


    function onInputChange(e) {
        setInputs({
            ...inputs,
            [e.target.name]: e.target.value
        })
    }

    return (
        <div className="login-container">

            <div className="titleAndIconContainer">
              <img className="login-icon" src="poll.png" alt="polling icon"/>
              <h1 className="login-title">PollAndVote</h1>
            </div>

            <form onSubmit={onSubmitForm} className="login-form">
                <input
                    className="login-input"
                    type="text"
                    name="email"
                    value={email}
                    onChange={e => onInputChange(e)}
                    placeholder="Email"
                />
                <input
                    className="login-input"
                    type="password"
                    name="password"
                    value={password}
                    onChange={e => onInputChange(e)}
                    placeholder="Password"
                />
                <button className="login-button">Login</button>
            </form>

            { !isRegistered && <p className="login-failed-text">Could not find a user with given credentials</p> }

            <Link to="/register" className="register-link">Go to register page</Link>
        </div>
    );
}