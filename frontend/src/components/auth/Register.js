import {Fragment, useContext, useState} from "react";
import {Link} from "react-router-dom"
import {AuthContext} from "../../App";
import "../../css/Register.css"

export default function Register() {
    const { setIsAuth } = useContext(AuthContext);
    const [ isRegistered, setIsRegistered ] = useState(true)

    const [inputs, setInputs] = useState({
        name: "",
        email: "",
        password: "",
    })

    const { name, email, password } = inputs;

    function onInputChange(e) {
        setInputs({
            ...inputs,
            [e.target.name]: e.target.value
        })
    }

    async function onSubmitForm(e) {
        e.preventDefault();

        try {
            const response = await fetch(`https://pollandvotelb.csmortal.store/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(inputs)
            }).then(res => res.json())

            console.log("response of register: " + JSON.stringify(response))

            if (response && response.success) {
                localStorage.setItem("token", response.token)
                localStorage.setItem("userId", response.userId)
                setIsAuth(true)
            } else {
                setIsRegistered(false)
            }
        } catch (error) {
            setIsRegistered(false)
        }
    }


    return (
        <div className="register-container">
            <h1 className="titleRegister">Register</h1>

            <form className="register-form" onSubmit={onSubmitForm}>
                <input
                    className="register-form-input"
                    type="text"
                    name="name"
                    value={name}
                    placeholder="Name"
                    onChange={e => onInputChange(e)}
                />
                <input
                    className="register-form-input"
                    type="text"
                    name="email"
                    value={email}
                    placeholder="Email"
                    onChange={e => onInputChange(e)}
                />
                <input
                    className="register-form-input"
                    type="password"
                    name="password"
                    value={password}
                    placeholder="Password"
                    onChange={e => onInputChange(e)}
                />
                <button className="register-form-register-btn">Register</button>
            </form>

            { !isRegistered && <p className="register-failed-text">This email has already been registered!</p> }

            <Link to="/login">Go to login page</Link>

        </div>

    )
}