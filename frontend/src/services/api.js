import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080',
})
api.interceptors.request.use(config=>{const token=localStorage.getItem('token');if(token)config.headers.Authorization=`Bearer ${token}`;return config})
api.interceptors.response.use(r=>r,error=>{if(error.response?.status===401&&!['/login','/api/auth/register'].includes(error.config?.url)){localStorage.removeItem('token');localStorage.removeItem('user');window.dispatchEvent(new Event('auth-changed'))}return Promise.reject(error)})
export const errorMessage=error=>error.response?.data?.message||Object.values(error.response?.data?.validationErrors||{})[0]||'Došlo je do greške. Pokušajte ponovo.'

export default api
