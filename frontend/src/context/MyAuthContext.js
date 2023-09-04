import React, { useState, createContext } from 'react';

export const MyAuthContext = createContext()

export const AuthContextProvider = props => {

    const [isAuthenticated, setIsAuthenticated] = useState(false)

    return (
        <MyAuthContext.Provider
            value={{
                isAuthenticated,
                setIsAuthenticated
            }}
        >
            {props.children}
        </MyAuthContext.Provider>
    )
}