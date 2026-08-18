import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './App.css'

function App() {

  return (
    <BrowserRouter>
      <div className='app-container'>
        <Routes>
          <Route path='/' element={<h1>Home page is coming soon....</h1>}/>
          <Route path='/room/:code' element={<h1>Room page is coming soon....</h1>}/>
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
