import {Fragment, useContext, useState} from "react";
import {Link} from "react-router-dom"
import {AuthContext} from "../../App";

export default function Register() {
    const { setIsAuth } = useContext(AuthContext);

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
            const response = await fetch("http://localhost:8080/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(inputs)
            }).then(res => res.json())

            console.log("response of register: " + JSON.stringify(response))

            if (response) {
                localStorage.setItem("token", response.token)
                localStorage.setItem("userId", response.userId)
                setIsAuth(true)
            }
        } catch (error) {
            console.error(error.message)
        }
    }


    return (
        <Fragment>
            <h1>Register</h1>

            <form onSubmit={onSubmitForm}>
                <input
                    type="text"
                    name="name"
                    value={name}
                    placeholder="Name"
                    onChange={e => onInputChange(e)}
                />
                <input
                    type="text"
                    name="email"
                    value={email}
                    placeholder="Email"
                    onChange={e => onInputChange(e)}
                />
                <input
                    type="password"
                    name="password"
                    value={password}
                    placeholder="Password"
                    onChange={e => onInputChange(e)}
                />
                <button>Register as new user</button>
            </form>

            <Link to="/login">Go to login page</Link>
        </Fragment>
    )
}