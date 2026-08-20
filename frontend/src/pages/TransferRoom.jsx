import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { transferServices } from "../services/api";
import { FileText, Send, Upload } from "lucide-react";

function TransferRoom(){
    const {code} = useParams();
    const [messages, setMessages] = useState([]);
    const [files, setFiles] = useState([]);
    const [uploading, setUploading] = useState(false);
    const [newMessage, setNewMessage] = useState('');

    useEffect(()=> {
        const fetchData = async() => {
            try{
                const msg = await transferServices.getMessages(code);
                const fls = await transferServices.getFiles(code);

                setMessages(msg);
                setFiles(fls);
            }catch(err){
                console.log("Error fetching room data",err);
            }
        }
        fetchData();
        const interval = setInterval(fetchData,5000);

        return () => clearInterval(interval)
    },[code]);

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if(!newMessage)return;
        await transferServices.sendMessage(code,newMessage);
        setNewMessage('');
    }

    const handleFileUpload = async(e)=>{
        const file = e.target.files[0];
        if(!file)return;

        try{
            const formData = new FormData();
            formData.append("file",file);
            setUploading(true);
            await transferServices.uploadFile(code,formData);
        }catch(err){
            console.log(err);
            alert("Error uploading file");
        }finally{
            setUploading(false);
        }
    }

    return(
        <div className="room-container">
            <div className="room-header-glass-card">
                <h2>Room Code: <span className="highlight">{code}</span></h2>
                <p>Share this code with a friend so they can join!</p>
            </div>

            <div className="room-grid">
                <div className="chat-section glass-card">
                    <h3>Chat</h3>
                    <div className="message-list">
                        {messages.map((msg)=> (
                            <div key={msg.id} className="message-bubble">
                                {msg.content}
                            </div>
                        ))}
                        {messages.length === 0 && <p className="empty-state">No messages yet...</p>}
                    </div>

                    <form className="message-input-area" onSubmit={handleSendMessage}>
                        <input type="text" placeholder="Type a message ...." value={newMessage} onChange={(e)=>setNewMessage(e.target.value)} />
                        <button type="submit" className="btn-primary" style={{width:'auto'}}>
                            <Send size={18}/>
                        </button>
                    </form>
                </div>

                <div className="file-section glass-card">
                    <h3>Files</h3>
                    <label className="upload-zone">
                        <input type="file" hidden onChange={handleFileUpload} disabled={uploading}/>
                        <Upload size={32}/>
                        <span>{uploading ? "Uploading..." : "Click to upload"}</span>
                    </label>

                    <div className="file-list">
                        {files.map((fl)=> (
                            <a key={fl.id} className="file-card" href={fl.fileUrl} target="_blank" rel="noreferrer">
                                <FileText size={24}/>
                                <div className="file-info">
                                    <span className="file-name">{fl.orginalFileName}</span>
                                    <span className="file-size">{(fl.size / 1024 / 1024).toFixed(2)} MB</span>
                                </div>
                            </a>
                        ))}

                        {files.length === 0 && <p className="empty-state">No files yet...</p>}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default TransferRoom