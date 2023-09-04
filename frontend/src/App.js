import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {createContext, useState} from "react";
import {AuthContextProvider} from "./context/MyAuthContext";
import PollDetail from "./components/poll/PollDetail"
import Dashboard from "./components/views/Dashboard";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import CreatePoll from "./components/views/CreatePoll";


export const AuthContext = createContext()
function App() {
  const [isAuth, setIsAuth] = useState(false);


  return (
    <AuthContext.Provider value={ {isAuth, setIsAuth} }>
      <BrowserRouter>
        <Routes>
          <Route index element={isAuth ? <Navigate to={'/dashboard'}/> : <Login/>}/>
          <Route path='/login' element={isAuth ? <Navigate to={'/dashboard'}/> : <Login/>}/>
          <Route path='/register' element={isAuth ? <Navigate to={'/dashboard'}/> : <Register/>}/>

          <Route path='/dashboard' element={isAuth ? <Dashboard/> : <Navigate to="/login" replace={true}/>}/>
          <Route path='/createPoll' element={isAuth ? <CreatePoll/> : <Navigate to="/login" replace={true}/>}/>
          <Route path='/viewPoll/:pollId' element={isAuth ? <PollDetail/> : <Navigate to="/login" replace={true}/>}/>
        </Routes>
      </BrowserRouter>
    </AuthContext.Provider>

  );
}

export default App;
