import './App.css';
import {Route, Routes} from "react-router-dom";
import Login from "./pages/login";
import Register from "./pages/register";
import Chat from "./pages/chat";

function App() {
  return <Routes>
    <Route path="/" element={<Login/>}/>
    <Route path="/login" element={<Login/>}/>
    <Route path="/register" element={<Register/>}/>
    <Route path="/chat" element={<Chat/>}/>
  </Routes>
}

export default App;
