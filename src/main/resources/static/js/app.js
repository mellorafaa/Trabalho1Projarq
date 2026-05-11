// ── API helpers ────────────────────────────────────────
const API_BASE = '';  // same origin; change to 'http://localhost:8080' for standalone

async function apiFetch(path) {
  const res = await fetch(API_BASE + path);
  if (!res.ok) throw new Error(`Erro ${res.status}: ${res.statusText}`);
  return res.json();
}

// ── Formatters ─────────────────────────────────────────
function formatPrice(cents) {
  return (cents / 100).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

// ── DOM helpers ────────────────────────────────────────
const $ = (id) => document.getElementById(id);

function setLoading() {
  $('productsGrid').innerHTML = '<p class="state-msg">Carregando...</p>';
}

function setError(msg) {
  $('productsGrid').innerHTML = `<p class="state-msg error">⚠️ ${msg}</p>`;
}

// ── Render: menu tabs ──────────────────────────────────
function renderTabs(menus, activeId) {
  $('menuTabs').innerHTML = menus
    .map(m => `
      <button class="tab-btn ${m.id === activeId ? 'active' : ''}"
              data-id="${m.id}">
        ${m.titulo}
      </button>`)
    .join('');

  $('menuTabs').querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => loadMenu(menus, Number(btn.dataset.id)));
  });
}

// ── Render: product cards ──────────────────────────────
function renderProducts(cardapio) {
  const header = $('cardapioHeader');
  const title  = $('cardapioTitle');
  header.style.display = 'block';
  title.textContent = cardapio.titulo;

  if (!cardapio.itens || cardapio.itens.length === 0) {
    $('productsGrid').innerHTML = '<p class="state-msg">Nenhum produto neste cardápio.</p>';
    return;
  }

  $('productsGrid').innerHTML = cardapio.itens
    .map(item => `
      <div class="product-card ${item.indicacao ? 'indicacao' : ''}">
        ${item.indicacao ? '<span class="chef-badge">⭐ Sugestão do Chef</span>' : ''}
        <div class="product-name">${item.descricao}</div>
        <div class="product-price">${formatPrice(item.preco)}</div>
      </div>`)
    .join('');
}

// ── Load a specific menu ───────────────────────────────
async function loadMenu(menus, id) {
  renderTabs(menus, id);
  setLoading();
  try {
    const cardapio = await apiFetch(`/cardapio/${id}`);
    renderProducts(cardapio);
  } catch (e) {
    setError(e.message);
  }
}

// ── Bootstrap ──────────────────────────────────────────
async function init() {
  setLoading();
  try {
    const menus = await apiFetch('/cardapio/lista');
    if (menus.length === 0) {
      $('menuTabs').innerHTML = '';
      $('productsGrid').innerHTML = '<p class="state-msg">Nenhum cardápio disponível.</p>';
      return;
    }
    await loadMenu(menus, menus[0].id);
  } catch (e) {
    setError(e.message);
  }
}

init();
