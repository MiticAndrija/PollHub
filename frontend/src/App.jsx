import { Route, Routes } from 'react-router-dom'
import MainLayout from './layouts/MainLayout'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import PollList from './pages/PollList';import PollDetails from './pages/PollDetails';import CreatePoll from './pages/CreatePoll';import EditPoll from './pages/EditPoll';import MyPolls from './pages/MyPolls';import AdminDashboard from './pages/AdminDashboard';import NotFound from './pages/NotFound';import ProtectedRoute from './components/ProtectedRoute'

function App() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route element={<ProtectedRoute />}><Route path="/polls" element={<PollList />} /><Route path="/polls/:id" element={<PollDetails />} /><Route path="/polls/create" element={<CreatePoll />} /><Route path="/polls/:id/edit" element={<EditPoll />} /><Route path="/my-polls" element={<MyPolls />} /></Route>
        <Route element={<ProtectedRoute admin />}><Route path="/admin" element={<AdminDashboard />} /></Route>
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  )
}

export default App
