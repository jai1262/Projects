import React from 'react'
import styled from 'styled-components'
import { mobile } from '../responsive'
import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { login } from '../redux/apiCalls'

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  background: linear-gradient(
      rgba(255, 255, 255, 0.2),
      rgba(255, 255, 255, 0.2)
    ),url("https://img.freepik.com/premium-photo/fashion-model-outdoor-portrait-tourist-woman-enjoying-sightseeing-lviv-girl-looking-ancient-atchitecture_106029-855.jpg?w=2000") center no-repeat;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: left;
  `
const Wrapper = styled.div`
  width: 25%;
  padding: 20px;
  background-color: #f5efe7;
  margin-left: 10%;
  border-radius: 5px;
  &:hover {
    box-shadow: rgba(173, 49, 49, 0.5) 0px 5px 15px;
    transform: scale(1.1);
    transition: all 0.5s ease;
  }
  ${mobile({width: "70%"})};
  `
  /**E9EDC9 FEFAE0 F1DEC9*/
const Title = styled.h1`
  font-size: 24px;
  font-weight: 400;
  `
const Form = styled.form`
  display: flex;
  flex-direction: column;
  `
const Input = styled.input`
  flex: 1;
  min-width: 40%;
  margin: 10px 0px;
  padding: 10px;
`
const Button = styled.button`
  width: 40%;
  border: none;
  padding: 15px 20px;
  background-color: #425858;
  color: white;
  cursor: pointer;

  &:disabled {
    color: green;
    cursor: not-allowed;
  }
  `
const Link = styled.a`
  margin: 10px 0px;
  font-size: 12px;
  text-decoration: underline;
  cursor: pointer;
`
const Error = styled.span`
  color: red;
`

function Login() {
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const dispatch = useDispatch();
  const {isFetching, error} = useSelector(state => state.user)

  function handleClick(e) {
    e.preventDefault();
    login(dispatch, {username, password})
  }

  return (
    <Container>
        <Wrapper>
            <Title>SIGN IN</Title>
            <Form>
                <Input placeholder="username" onChange={((e) => setUserName(e.target.value))}/>
                <Input placeholder="password" onChange={((e) => setPassword(e.target.value))}/>
                <Button onClick={handleClick} disabled={isFetching}>LOGIN</Button>
                {error && <Error>Something went wrong...</Error>}
                <Link>DO NOT YOU REMEMBER THE PASSWORD?</Link>
                <Link to="/register">CREATE A NEW ACCOUNT</Link>
            </Form>
        </Wrapper>
    </Container>
  )
}

export default Login