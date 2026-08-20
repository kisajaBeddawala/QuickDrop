import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './App.css'
import Home from './pages/Home'
import TransferRoom from './pages/TransferRoom'


function App() {

  return (
    <BrowserRouter>
      <div className='app-container'>
        <Routes>
          <Route path='/' element={<Home/>}/>
          <Route path='/room/:code' element={<TransferRoom/>}/>
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
