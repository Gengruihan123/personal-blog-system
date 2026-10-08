window.BLOG_CONFIG = Object.freeze({
    // npm run dev 时走 Vite 代理；npm run preview 或独立部署时直连后端。
    API_BASE_URL: location.port === '5173'
        ? '/api'
        : `http://${location.hostname === '127.0.0.1' ? '127.0.0.1' : 'localhost'}:8080/api`
});

window.blogApi = async function (path, options = {}) {
    const response = await fetch(`${window.BLOG_CONFIG.API_BASE_URL}${path}`, {
        credentials: 'include',
        ...options,
        headers: {
            ...(options.body ? {'Content-Type': 'application/json'} : {}),
            ...(options.headers || {})
        }
    });
    const data = await response.json().catch(() => ({}));
    if (!response.ok) throw new Error(data.message || '请求失败，请确认后端已启动');
    return data;
};

window.escapeHtml = function (value) {
    return String(value ?? '').replace(/[&<>"']/g, char => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
};

window.showMessage = function (message, error = false) {
    const element = document.createElement('div');
    element.className = `fixed right-5 bottom-5 z-[100] px-5 py-3 rounded-lg shadow-xl text-white ${error ? 'bg-red-600' : 'bg-gray-900'}`;
    element.textContent = message;
    document.body.appendChild(element);
    setTimeout(() => element.remove(), 3000);
};
