import React from "react";
import axiosInstanceUser from "../axiosUser";
import { Button, Container, TextField, Grid, Box, Typography } from "@mui/material";
import { LoginRounded } from "@mui/icons-material";

class Login extends React.Component {
    constructor() {
        super();
        this.state = {
            username: "",
            password: "",
        };
    }

    handleInput = (event) => {
        const { value, name } = event.target;
        this.setState({
            [name]: value,
        });
    };

    onSubmitLogin = (event) => {
        event.preventDefault(); // Prevent the default form submission.
        const { username, password } = this.state;

        axiosInstanceUser
            .post("/user/login", { username, password })
            .then((res) => {
                console.log(res.data);
                if (res.data) {
                    sessionStorage.id = res.data.id;
                    sessionStorage.username = res.data.username;
                    window.location.replace("/chat");
                    alert("Logged in!");
                } else {
                    alert("Invalid credentials");
                }
            })
            .catch((error) => {
                console.log(error);
                alert("Error!");
            });
    };

    render() {
        return (
            <Container maxWidth="sm">
                <div>
                    <Grid>
                        <form onSubmit={this.onSubmitLogin}>
                            <Box sx={{ pt: 5 }} />
                            <Typography variant="h5" sx={{ m: 2 }}>
                                Welcome to the system!
                            </Typography>
                            <Box sx={{ pt: 35 }}>
                                <TextField
                                    variant="outlined"
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="username"
                                    label="Username"
                                    name="username"
                                    autoComplete="username"
                                    onChange={this.handleInput}
                                    autoFocus
                                />
                                <TextField
                                    variant="outlined"
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="password"
                                    label="Password"
                                    type="password"
                                    name="password"
                                    autoComplete="current-password"
                                    onChange={this.handleInput}
                                />
                                <Button
                                    startIcon={<LoginRounded />}
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                >
                                    Login
                                </Button>
                            </Box>
                        </form>
                        <Box sx={{ pt: 2 }}>
                            <Typography variant="body2">
                                Don't have an account?{" "}
                                <a href="/register">Register here</a>
                            </Typography>
                        </Box>
                    </Grid>
                </div>
            </Container>
        );
    }
}

export default Login;
