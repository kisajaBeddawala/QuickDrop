import { LogIn, UploadCloud } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { transferServices } from "../services/api";


function Home(){

    const [joinCode,setJoinCode] = useState('');
    const navigate = useNavigate();

    const handleCreateRoom = async () => {
        try{
            const room = await transferServices.createTransfer();
            navigate(`/room/${room.code}`);
        }catch(error){
            console.log("failed to create room!");
        }
    }

    const handleJoinRoom = async (e) => {
        e.preventDefault();
        if(!joinCode)return;
        try{
            await transferServices.joinTranfer(joinCode);
            navigate(`/room/${joinCode}`)
        }catch(err){
            console.log("Invalid code or room doesn't exist!")
        }
    }

    return(
        <div className="glass-card">
            <h1>QuickDrop</h1>
            <p className="subtitle">Share file and messages instantly</p>

            <div>
                <button onClick={handleCreateRoom} className="btn-primary">
                    <UploadCloud size={20}/> Create New Room
                </button>
            </div>
            <div className="divider"><span>OR</span></div>
            <form onSubmit={handleJoinRoom}>
                <input type="text" placeholder="Enter Room Code (e.g. ABc321)" value={joinCode} onChange={(e) => setJoinCode(e.target.value)}/>
                <button type="submit" className="btn-secondary">
                    <LogIn size={20}/> Join
                </button>
            </form>
        </div>
    );
}

export default Home;