import React, { useState, useEffect } from "react";
import axiosInstanceUser from "../axiosUser";
import axiosMessage from '../axiosMessage';
import { Grid, Container, Paper, Typography, List, ListItem, ListItemText, ListItemIcon, TextField, Button } from '@mui/material';

const Chat = () => {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const [selectedChannel, setSelectedChannel] = useState("broadcast");

    useEffect(() => {
        // Fetch users when component mounts
        fetchUsers();
    }, []);

    useEffect(() => {
        // Fetch messages when selected user or channel changes
        fetchMessages();
    }, [selectedUser, selectedChannel]);

    const fetchUsers = () => {
        axiosInstanceUser.get("/user")
            .then(response => setUsers(response.data))
            .catch(error => console.error("Error fetching users:", error));
    };

    const fetchMessages = () => {
        // Determine whether to fetch broadcast messages or potentially filter private messages
        if (selectedChannel === "broadcast") {
            // Fetch all broadcast messages
            axiosMessage.get("/messages")
                .then(response => {
                    const transformedMessages = response.data.map(message => ({
                        sender: message.sender.username,
                        content: message.content,
                        sentAt: message.sentAt
                    }));
                    setMessages(transformedMessages);
                })
                .catch(error => console.error("Error fetching messages:", error));
        } else {
            // Fetch all messages and filter for those between the logged user and the selected user
            console.log(sessionStorage.id + " " + selectedUser.id);
            axiosMessage.get("/messages")
                .then(response => {
                    const loggedUserId = parseInt(sessionStorage.getItem('id'));
                    const selectedUserId = selectedUser.id;
                    const filteredMessages = response.data.filter(message =>
                        (message.sender.id === loggedUserId && message.recipient && message.recipient.id === selectedUserId) ||
                        (message.sender.id === selectedUserId && message.recipient && message.recipient.id === loggedUserId)
                    ).map(message => ({
                        sender: message.sender.username,
                        content: message.content,
                        sentAt: message.sentAt
                    }));
                    setMessages(filteredMessages);
                })
                .catch(error => console.error("Error fetching messages:", error));
        }
    };


    const handleUserSelect = (recipient) => {
        setMessages([]);
        if (recipient === "broadcast") {
            setSelectedChannel("broadcast");
            setSelectedUser(null);
        } else {
            const user = users.find(user => user.id === parseInt(recipient));
            setSelectedUser(user);
            setSelectedChannel(null);
        }
    };

    const handleSendMessage = () => {
        const sender = sessionStorage.getItem('username');
        const content = newMessage;

        let messageData, endpoint;
        if (selectedUser) {
            // For user-to-user message
            const recipient = selectedUser.username; // Assuming you need to send username, not ID
            messageData = {
                sender,
                recipient,
                content
            };
            endpoint = "/messages/single";
        } else {
            // For broadcast message
            messageData = {
                sender,
                content
            };
            endpoint = "/messages/all";
        }

        axiosMessage.post(endpoint, messageData)
            .then(() => {
                setNewMessage("");
                fetchMessages();
            })
            .catch(error => console.error("Error sending message:", error));
    };

    return (
        <Container>
            <Grid container spacing={2}>
                <Grid item xs={3}>
                    <Paper>
                        <Typography variant="h6" sx={{ m: 4 }}>
                            Select Channel
                        </Typography>
                        <List>
                            <ListItem button onClick={() => handleUserSelect("broadcast")} selected={selectedChannel === "broadcast"}>
                                <ListItemIcon>📢</ListItemIcon>
                                <ListItemText primary="Broadcast" />
                            </ListItem>
                            {users.filter(user => user.id !== parseInt(sessionStorage.getItem('id')))
                                .map(user => (
                                    <ListItem key={user.id} button onClick={() => handleUserSelect(user.id)} selected={selectedUser && selectedUser.id === user.id}>
                                        <ListItemText primary={user.username} />
                                    </ListItem>
                                ))
                            }
                        </List>
                    </Paper>
                </Grid>
                <Grid item xs={9}>
                    <Paper>
                        <div className="chat-content">
                            <ul className="chat-messages" style={{ listStyleType: 'none', display: 'flex', flexDirection: 'column' }}>
                                {messages.map((message, index) => (
                                    <li key={index}>
                                        {`${message.sender}: ${message.content}`}
                                    </li>
                                ))}
                            </ul>
                            <div className="send-message">
                                <TextField
                                    variant="outlined"
                                    label="Type your message"
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    fullWidth
                                    sx={{ marginBottom: '8px' }}
                                />
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={handleSendMessage}
                                    sx={{ marginBottom: '8px' }}
                                >
                                    Send
                                </Button>
                            </div>
                        </div>
                    </Paper>
                </Grid>
            </Grid>
        </Container>
    );
};

export default Chat;
