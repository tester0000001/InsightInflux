let searchByCode = false;
function addProduct() {
    // Gather data from form fields
    var code = document.getElementById('code').value;
    var name = document.getElementById('name').value;
    var priceEur = document.getElementById('priceEur').value;
    var description = document.getElementById('description').value;

    // Construct JSON payload
    var data = JSON.stringify({
        code: code,
        name: name,
        priceEur: priceEur,
        description: description
    });

    // Send POST request to backend
    fetch('/api/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Request failed!');
    }).then(data => {
        console.log(data);
        fetchProducts(); // Refresh the list of products
        fetchPopularProducts();
    }).catch(error => {
        console.error(error);
    });
}

function fetchPopularProducts() {
    fetch('/api/products/popular').then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Request failed!');
    }).then(popularProducts => {
        const popularProductsElement = document.getElementById('popular');
        popularProductsElement.innerHTML = '';
        if (popularProducts && popularProducts.length) {
            popularProducts.forEach(product => {
                const li = document.createElement('li');
                li.className = 'popular-product-item';
                li.innerHTML = `
                    <strong>${product.name}</strong>
                    <p>Average Rating: ${product.averageRating.toFixed(1)}</p>
                `;
                popularProductsElement.appendChild(li);
            });
        } else {
            popularProductsElement.innerHTML = '<li>No popular products found.</li>';
        }
    }).catch(error => {
        console.error(error);
    });
}

function addReview() {
    var productId = document.getElementById('productId').value;
    var reviewer = document.getElementById('reviewer').value;
    var text = document.getElementById('reviewText').value;
    var rating = document.getElementById('rating').value;

    var data = JSON.stringify({
        productId: productId,
        reviewer: reviewer,
        text: text,
        rating: rating
    });

    fetch('/api/reviews', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Request failed!');
    }).then(data => {
        console.log(data);
        fetchProducts();
        fetchPopularProducts();
        fetchReviews(productId);
    }).catch(error => {
        console.error(error);
    });
}

function fetchReviews(productId) {
    fetch(`/api/reviews?productId=${productId}`).then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Request failed!');
    }).then(reviews => {
        const productItem = document.querySelector(`.product-item[data-product-id="${productId}"]`);
        const reviewsList = productItem.querySelector('.reviews');
        reviewsList.innerHTML = '';

        reviews.forEach(review => {
            const li = document.createElement('li');
            li.className = 'review-item';
            li.innerHTML = `
                <strong>${review.reviewer}</strong>
                <p>${review.text}</p>
                <p>Rating: ${review.rating}</p>
            `;
            reviewsList.appendChild(li);
        });
    }).catch(error => {
        console.error(error);
    });
}

function fetchProducts() {
    fetch('/api/products').then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Request failed!');
    }).then(products => {
        const productsListElement = document.getElementById('products');

        productsListElement.innerHTML = '';

        products.forEach(product => {
            const li = document.createElement('li');
            li.className = 'product-item';
            li.setAttribute('data-product-id', product.id);
            li.innerHTML = `
                <strong>${product.name}</strong>
                <p>Code: ${product.code}</p>
                <p>Price: €${product.priceEur.toFixed(2)} / $${product.priceUsd.toFixed(2)}</p>
                <p>${product.description}</p>
                <button onclick="showReviewForm(${product.id})" class="btn btn-secondary">Add Review</button>
                <ul class="reviews"></ul>
            `;
            productsListElement.appendChild(li);
            fetchReviews(product.id);
        });
    }).catch(error => {
        console.error(error);
    });
}

function showReviewForm(productId) {
    document.getElementById('reviewForm').style.display = 'block';
    document.getElementById('productId').value = productId;
}

function fetchExchangeRate() {
    fetch('/api/exchange-rate')
        .then(response => response.json())
        .then(data => {
            const exchangeRate = data.rate;
            const currency = data.currency;
            document.getElementById('exchangeRate').textContent = `EUR to ${currency}: ${exchangeRate.toFixed(4)}`;
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
            document.getElementById('exchangeRate').textContent = 'EUR to USD: Not available';
        });
}

// Toggle dark mode
document.getElementById('darkModeToggle').addEventListener('click', function () {
    document.body.classList.toggle('dark-mode');
});

// Save dark mode preference
document.getElementById('darkModeToggle').addEventListener('click', function () {
    const isDarkMode = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDarkMode ? 'enabled' : 'disabled');
});

// Check for saved preference
document.addEventListener('DOMContentLoaded', (event) => {
    if (localStorage.getItem('darkMode') === 'enabled') {
        document.body.classList.add('dark-mode');
    }
});

function searchProducts() {
    var searchTerm = document.getElementById('searchInput').value;
    var queryParam = searchByCode ? `code=${searchTerm}` : `name=${searchTerm}`;

    fetch(`/api/products?${queryParam}`).then(response => response.json())
        .then(products => {
            updateProductList(products);
        }).catch(error => {
        console.error('Error searching for products:', error);
    });
}

function toggleSearchMode() {
    searchByCode = !searchByCode;
    document.getElementById('toggleSearchModeBtn').textContent = searchByCode ? 'Search by Code' : 'Search by Name';
}

function updateProductList(products) {
    const productsListElement = document.getElementById('products');
    productsListElement.innerHTML = ''; // Clear current products list

    products.forEach(product => {
        const li = document.createElement('li');
        li.className = 'product-item';
        li.setAttribute('data-product-id', product.id);
        li.innerHTML = `
            <strong>${product.name}</strong>
            <p>Code: ${product.code}</p>
            <p>Price: €${parseFloat(product.priceEur).toFixed(2)} / $${parseFloat(product.priceUsd).toFixed(2)}</p>
            <p>${product.description}</p>
            <button onclick="showReviewForm(${product.id})" class="btn btn-secondary">Add Review</button>
            <ul class="reviews"></ul>
        `;
        productsListElement.appendChild(li);
    });
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('toggleSearchModeBtn').textContent = searchByCode ? 'Search by Code' : 'Search by Name';
    fetchProducts();
    fetchPopularProducts();
    fetchExchangeRate();
});