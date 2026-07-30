import { NavLink, Outlet } from 'react-router-dom'

function MainLayout() {
  return (
    <>
      <header>
        <nav>
          <NavLink to="/">Početna</NavLink>
          <NavLink to="/login">Prijava</NavLink>
          <NavLink to="/register">Registracija</NavLink>
        </nav>
      </header>
      <main>
        <Outlet />
      </main>
    </>
  )
}

export default MainLayout
