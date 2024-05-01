import React from "react";
import axiosInstanceUser from "../axiosUser";
import { Button, Container, TextField, Grid, Box, Typography } from "@mui/material";

class Register extends React.Component {
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

    onSubmitRegister = (event) => {
        event.preventDefault(); // Prevent the default form submission.
        const { username, password } = this.state;

        axiosInstanceUser
            .post("/user", { username, password })
            .then((res) => {
                console.log(res.data);
                if (res.data > 0) {
                    alert("Registration successful!");
                    // Redirect the user to the login page or any other page as needed
                    window.location.replace("/login");
                } else {
                    alert("Registration failed. Please try again.");
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
                        <form onSubmit={this.onSubmitRegister}>
                            <Box sx={{ pt: 5 }} />
                            <Typography variant="h5" sx={{ m: 2 }}>
                                Register New User
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
                                    autoComplete="new-password"
                                    onChange={this.handleInput}
                                />
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                >
                                    Register
                                </Button>
                            </Box>
                        </form>
                    </Grid>
                </div>
            </Container>
        );
    }
}

export default Register;
