import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8086/api/user',
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            const refreshToken = localStorage.getItem('refreshToken');

            if (refreshToken) {
                try {
                    const { data } = await axiosInstance.get('/auth/refresh', {
                        headers: { Authorization: `Bearer ${refreshToken}` },
                    });

                    localStorage.setItem('accessToken', data.accessToken);
                    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${data.accessToken}`;

                    return axiosInstance(originalRequest);
                } catch (refreshError) {
                    console.error('Refresh token failed', refreshError);
                    logout();
                }
            } else {
                logout();
            }
        }

        return Promise.reject(error);
    }
);

const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    window.location.href = '/signin';
};

const UserApi = {
    signup: (email, name, password) => axiosInstance.post('/signup', { email, name, password }),
    signin: (email, password) => axiosInstance.post('/signin', { email, password }),
    logout: () => axiosInstance.post('/logout'),
};

export default UserApi;
