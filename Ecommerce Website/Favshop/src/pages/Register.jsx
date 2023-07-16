import React from 'react'
import styled from 'styled-components'
import { mobile } from '../responsive'
import { useState } from 'react'
import { publicRequest } from '../requestMethods'
import { useNavigate } from 'react-router'

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  background: linear-gradient(
      rgba(255, 255, 255, 0.2),
      rgba(255, 255, 255, 0.2)
    ),url("https://img.freepik.com/free-photo/pretty-young-woman-with-her-arms-crossed-looking-away-against-pink-background_23-2148178131.jpg?w=1380&t=st=1686288782~exp=1686289382~hmac=c0266cea7129e56ea450e01c5c9c871077369cd442f2df3dba677dece07ca4c5") center no-repeat;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  `
const Wrapper = styled.div`
  width: 30%;
  padding: 20px;
  background-color: #F0EEED;
  margin-left: 100px;
  border-radius: 5px;
  &:hover {
    box-shadow: rgba(173, 49, 49, 0.5) 0px 5px 15px;
    transform: scale(1.1);
    transition: all 0.5s ease;
  }
  ${mobile({width: "70%", marginLeft: "10px"})}
  `
  /**ebecee F8F6F4 F9F5F6 EEEEEE C4B0FF FDCEDF 9DB2BF DDE6ED*/
const Title = styled.h1`
  font-size: 24px;
  font-weight: 600;
  `
const Form = styled.form`
  display: flex;
  flex-wrap: wrap;
  `
const Input = styled.input`
  flex: 1;
  min-width: 40%;
  margin: 20px 10px 0px 0px;
  padding: 10px;
`
const Agreement = styled.span`
  font-size: 12px;
  margin: 20px 0px;
`
const Button = styled.button`
  width: 40%;
  border: none;
  padding: 15px 20px;
  background-color: teal;
  color: white;
  cursor: pointer;
  `

function Register() {
  const [nusername, setNUsername] = useState("");
  const [nemail, setNEmail] = useState("");
  const [npassword, setNPassword] = useState("");
  const navigate = useNavigate();
  const handleClick = async () => {
    const res = await publicRequest.post("/auth/register", {
      username: nusername,
      email: nemail,
      password: npassword
    });
    res.status(200).json(res.data);
    navigate("/login", {state: {replace: true}});
  };

  return (
    <Container>
        <Wrapper>
            <Title>CREATE AN ACCOUNT</Title>
            <Form>
                <Input placeholder="name"/>
                <Input placeholder="last name"/>
                <Input placeholder="username" onChange={(e) => setNUsername(e.target.value)}/>
                <Input placeholder="email" onChange={(e) => setNEmail(e.target.value)}/>
                <Input placeholder="password"/>
                <Input placeholder="confirm password" onChange={(e) => setNPassword(e.target.value)}/>
                <Agreement>
                By creating an account, I consent to the processing of my personal
            data in accordance with the <b>PRIVACY POLICY</b>
                </Agreement>
                <Button onClick={handleClick}>CREATE</Button>
            </Form>
        </Wrapper>
    </Container>
  )
}
/**
 *
 *
 *
 *
 */
export default Register