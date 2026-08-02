import api from './api';export const login=data=>api.post('/login',data).then(r=>r.data);export const register=data=>api.post('/api/auth/register',data).then(r=>r.data)
