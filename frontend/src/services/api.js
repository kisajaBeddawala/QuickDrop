import axios from "axios";
import { Aperture } from "lucide-react";

const API_URL = '/api/transfers';

export const transferServices = {
    // create a new transfer room
    createTransfer: async () => {
        const response = await axios.post(API_URL);
        return response.data;
    },

    // join an existing transfer room
    joinTranfer: async (code) => {
        const response = await axios.post(`${API_URL}/join`,{code});
        return response.data;
    },

    // get all messages for a room
    getMessages: async (code) => {
        const response = await axios.get(`${API_URL}/${code}/messages`);
        return response.data;
    },

    // send a message
    sendMessage: async (code,content) => {
        const response = await axios.post(`${API_URL}/${code}/messages`,{content});
        return response.data;
    },

    // get all files from a room
    getFiles: async (code) => {
        const response = await axios.get(`${API_URL}/${code}/files`);
        return response.data;
    },

    // upload a file to a room
    // we use formdata here it matches the @RequestParam("file") in Spring Boot.
    uploadFile: async (code, file) => {
        const formData = new FormData();
        formData.append('file',file);

        const response = await axios.post(`${API_URL}/${code}/files`,{formData}, 
            {
                headers : {"Content-Type": "multipart/form-data"}
            }
        )

        return response.data;
    }
}