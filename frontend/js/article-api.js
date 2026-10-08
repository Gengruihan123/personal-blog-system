(function () {
    const api = window.blogApi;
    const escape = window.escapeHtml;
    const id = new URLSearchParams(location.search).get('id') || '1';
    const formatDate = value => new Date(value).toLocaleDateString('zh-CN');

    async function loadArticle() {
        const article = await api(`/articles/${id}`);
        document.title = `${article.title} - 我的个人博客`;
        document.getElementById('article-title').textContent = article.title;
        document.getElementById('article-author').textContent = article.authorNickname;
        document.getElementById('article-date').textContent = `发布于 ${formatDate(article.publishedAt)}`;
        document.getElementById('article-cover').src = article.cover || `https://picsum.photos/1200/600?random=${article.id}`;
        document.getElementById('article-badges').innerHTML = `<a href="index.html" class="bg-primary/10 text-primary text-sm font-medium px-3 py-1 rounded-full">${escape(article.categoryName)}</a>${article.tags.map(tag => `<a href="index.html" class="bg-blue-50 text-blue-700 text-sm font-medium px-3 py-1 rounded-full"># ${escape(tag.name)}</a>`).join('')}`;
        document.getElementById('article-meta').innerHTML = `<span class="flex items-center mr-4"><i class="fa fa-eye mr-1"></i>${article.viewCount} 阅读</span><span class="flex items-center mr-4"><i class="fa fa-comment-o mr-1"></i>${article.commentCount} 评论</span><span class="flex items-center"><i class="fa fa-clock-o mr-1"></i>${Math.max(1, Math.ceil(article.content.replace(/<[^>]*>/g,'').length / 500))} 分钟阅读</span>`;
        document.getElementById('article-content').innerHTML = article.content;
        document.getElementById('bottom-tags').innerHTML = `<span class="text-gray-500 mr-2">标签：</span>${article.tags.map(tag => `<span class="bg-blue-50 text-blue-700 text-sm font-medium px-3 py-1 rounded-full mr-2">${escape(tag.name)}</span>`).join('')}`;
    }

    async function loadComments() {
        const comments = await api(`/articles/${id}/comments`);
        document.getElementById('comment-list').innerHTML = comments.length ? comments.map(comment => `<div class="pb-5 border-b border-gray-100"><div class="flex justify-between mb-2"><strong>${escape(comment.userNickname)}</strong><span class="text-sm text-gray-500">${formatDate(comment.createdAt)}</span></div><p class="text-gray-700">${escape(comment.content)}</p></div>`).join('') : '<p class="text-gray-500">暂无评论，欢迎发表第一条评论。</p>';
    }

    document.getElementById('comment-form').addEventListener('submit', async event => { event.preventDefault(); try { await api(`/articles/${id}/comments`, {method:'POST', body:JSON.stringify({content:document.getElementById('comment-content').value})}); showMessage('评论已提交，审核后显示'); event.currentTarget.reset(); } catch (error) { showMessage(`${error.message}；如未登录，请先登录`, true); } });
    document.querySelectorAll('a[href="/"]').forEach(a => a.href='index.html');
    document.querySelectorAll('a[href="/dashboard"]').forEach(a => a.href='admin.html');
    document.querySelectorAll('a[href="/profile"]').forEach(a => a.href='profile.html');
    Promise.all([loadArticle(), loadComments()]).catch(error => showMessage(error.message, true));
})();
