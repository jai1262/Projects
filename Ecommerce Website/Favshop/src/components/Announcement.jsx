import React from 'react'
import styled from 'styled-components'

function Announcement() {
    const Container = styled.div`
        height: 35px;
        background-color: teal;
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        margin-top: -5px;
    `
  return (
    <Container>
        Super Deal! Free Shipping on Orders Over $50
    </Container>
  )
}

export default Announcement