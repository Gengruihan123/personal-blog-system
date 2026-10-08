(function () {
    const api = window.blogApi;
    const escape = window.escapeHtml;
    const articleList = document.getElementById('article-list');
    const categoryList = document.getElementById('category-list');
    const tagList = document.getElementById('tag-list');

    const formatDate = value => value ? new Date(value).toLocaleDateString('zh-CN') : '';
    const articleUrl = id => `article-detail.html?id=${id}`;

    function card(article, index) {
        const cover = article.cover || `https://picsum.photos/600/400?random=${index + 1}`;
        const category = article.categoryName || '未分类';
        return `<article class="bg-white rounded-xl shadow-md overflow-hidden card-hover">
            <div class="relative"><img src="${escape(cover)}" alt="文章封面图" class="w-full h-48 object-cover"><div class="absolute top-4 left-4 bg-primary text-white text-xs font-semibold px-3 py-1 rounded-full">${escape(category)}</div></div>
            <div class="p-6"><div class="flex items-center text-sm text-gray-500 mb-3"><span class="flex items-center"><i class="fa fa-calendar-o mr-1"></i>${formatDate(article.publishedAt)}</span><span class="mx-2">•</span><span class="flex items-center"><i class="fa fa-eye mr-1"></i>${article.viewCount}</span><span class="mx-2">•</span><span class="flex items-center"><i class="fa fa-comment-o mr-1"></i>${article.commentCount}</span></div>
            <h3 class="text-xl font-bold mb-3 line-clamp-2"><a href="${articleUrl(article.id)}" class="text-gray-900 hover:text-primary transition-colors">${escape(article.title)}</a></h3>
            <p class="text-gray-600 mb-4 line-clamp-3">${escape(article.summary)}</p><a href="${articleUrl(article.id)}" class="inline-flex items-center text-primary font-medium hover:underline">阅读更多 <i class="fa fa-long-arrow-right ml-2"></i></a></div></article>`;
    }

    async function loadArticles(query = '') {
        const data = await api(`/articles?page=0&size=6&keyword=${encodeURIComponent(query)}`);
        articleList.innerHTML = data.content.length ? data.content.map(card).join('') : '<p class="col-span-3 text-center text-gray-500 py-12">没有找到相关文章</p>';
    }

    async function loadTaxonomies() {
        const [categories, tags] = await Promise.all([api('/categories'), api('/tags')]);
        categoryList.innerHTML = categories.map((category, index) => `<li><a href="#latest" data-category="${category.id}" class="flex justify-between items-center py-2 px-3 rounded-lg hover:bg-gray-50 transition-colors"><span class="flex items-center"><i class="fa ${['fa-laptop','fa-coffee','fa-book','fa-plane','fa-lightbulb-o'][index % 5]} text-primary mr-3"></i>${escape(category.name)}</span><span class="bg-gray-100 text-gray-800 text-xs font-medium px-2.5 py-0.5 rounded-full">${category.articleCount}</span></a></li>`).join('');
        tagList.innerHTML = tags.map(tag => `<a href="#latest" data-tag="${tag.id}" class="px-4 py-2 bg-blue-50 text-blue-700 rounded-full text-sm hover:bg-blue-100 transition-colors"># ${escape(tag.name)}</a>`).join('');
        document.querySelectorAll('[data-category]').forEach(link => link.addEventListener('click', async event => { event.preventDefault(); const data = await api(`/articles?page=0&size=20&categoryId=${link.dataset.category}`); articleList.innerHTML = data.content.map(card).join(''); articleList.scrollIntoView({behavior:'smooth'}); }));
        document.querySelectorAll('[data-tag]').forEach(link => link.addEventListener('click', async event => { event.preventDefault(); const data = await api(`/articles?page=0&size=20&tagId=${link.dataset.tag}`); articleList.innerHTML = data.content.map(card).join(''); articleList.scrollIntoView({behavior:'smooth'}); }));
    }

    document.querySelectorAll('input[placeholder="搜索文章..."]').forEach(input => input.addEventListener('keydown', event => { if (event.key === 'Enter') { event.preventDefault(); loadArticles(input.value.trim()).catch(error => showMessage(error.message, true)); } }));
    document.getElementById('subscribe-form').addEventListener('submit', async event => { event.preventDefault(); try { await api('/subscriptions', {method:'POST', body:JSON.stringify({email:new FormData(event.currentTarget).get('email')})}); showMessage('订阅成功'); event.currentTarget.reset(); } catch (error) { showMessage(error.message, true); } });

    document.querySelectorAll('a[href="/"]').forEach(a => a.href = 'index.html');
    document.querySelectorAll('a[href="/dashboard"]').forEach(a => a.href = 'admin.html');
    document.querySelectorAll('a[href="/profile"]').forEach(a => a.href = 'profile.html');
    document.querySelectorAll('a[href="/logout"]').forEach(a => { a.href='#latest'; a.addEventListener('click', async e => { e.preventDefault(); await api('/auth/logout',{method:'POST'}); location.reload(); }); });
    document.querySelectorAll('a[href="/categories"],a[href="/tags"],a[href="/archive"],a[href="/about"],a[href="/articles"]').forEach(a => a.href = '#latest');
    document.querySelector('section.py-16.bg-white')?.setAttribute('id','latest');

    Promise.all([loadArticles(), loadTaxonomies(), api('/auth/me').then(user => {
        if (!user || !user.id) document.querySelectorAll('#user-menu a').forEach((a,index) => { if(index===0){a.textContent='登录';a.href='login.html';} });
    })]).catch(error => showMessage(error.message, true));
})();
