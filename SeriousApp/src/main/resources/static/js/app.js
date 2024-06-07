let openShopping = document.querySelector('.shopping');
let closeShopping = document.querySelector('.closeShopping');
let shopping_list = document.querySelector('.list');
let listCard = document.querySelector('.listCard');
let body = document.querySelector('body');
let total = document.querySelector('.total');
let quantity = document.querySelector('.quantity');


  let pu = document.getElementById('paymentUpdated');
  
  let fgh = document.getElementById('fch');
  let gh = document.getElementById('nt');
  
  
 
  
body.classList.add('active');


let products = [
    {
        id: 1,
        name: 'Gumbo',
        image: '1.PNG',
        price: 320
    },
    {
        id: 2,
        name: 'Chilli Potato',
        image: '2.PNG',
        price: 350
    },
    {
        id: 3,
        name: 'Green Salad',
        image: '3.PNG',
        price: 290
    },
    {
        id: 4,
        name: 'Mushroom Soup',
        image: '4.PNG',
        price: 523
    },
    {
        id: 5,
        name: 'Key lime pie',
        image: '5.PNG',
        price: 720
    },
    {
        id: 6,
        name: 'Pizza',
        image: '6.PNG',
        price: 499
    },
    {
        id: 7,
        name: 'Papdi Chaat',
        image: '7.PNG',
        price: 190
    },
    {
        id: 8,
        name: 'Dahi bada',
        image: '8.PNG',
        price: 170
    },
    {
        id: 9,
        name: 'North Thali',
        image: '9.PNG',
        price: 960
    },
    {
        id: 10,
        name: 'Diet Thali',
        image: '10.PNG',
        price: 1220
    },
    {
        id: 11,
        name: 'Sprouts',
        image: '11.PNG',
        price: 90
    },
    {
        id: 12,
        name: 'Cornfilled dish',
        image: '12.PNG',
        price: 324
    }
];
let listCards  = [];
function initApp(){
    products.forEach((value, key) =>{
        let newDiv = document.createElement('div');
        newDiv.classList.add('item');
        console.log(value.image);
           
        newDiv.innerHTML = `
           <img src="/Images/shopping-images/${value.image}" alt="no" style="height:123px;width:130px;">
            <div class="title">${value.name}</div>
            <div class="price">$${value.price.toLocaleString()}</div>
            <button style="cursor:pointer" onclick="addToCard(${key})">Add To Card</button>`;
            shopping_list.appendChild(newDiv);
    })
}
initApp();
function addToCard(key){
    if(listCards[key] == null){
        // copy product form list to list card
        listCards[key] = JSON.parse(JSON.stringify(products[key]));
        listCards[key].quantity = 1;
    }
    reloadCard();
}
function reloadCard(){
    listCard.innerHTML = '';
    
    var initialPrice = document.getElementById('initial');
    var ip = Number(initialPrice.value);
    
    var seatPrice = document.getElementById('priceSeatId');
    var sp = Number(seatPrice.value);
    
    var foodPrice = document.getElementById('foodPrice');
    var fp = Number(foodPrice.value);
    
    var totallingPrice = document.getElementById("totalss");
    var tpp = Number(totallingPrice.value);
    
    
    
    
    let count = 0;
    let totalPrice = 0;
    listCards.forEach((value, key)=>{
        totalPrice = totalPrice + value.price;
        count = count + value.quantity;
        if(value != null){
            let newDiv = document.createElement('li');
            newDiv.innerHTML = `
                
                <div style="color:black;">${value.name}</div>
                
                <div style="color:black;margin-left:20px;">  $${value.price.toLocaleString()} </div>
                <div style="color:black;margin-left:42px;">
                    <button onclick="changeQuantity(${key}, ${value.quantity - 1})"> <span style="font-size:30px;width:100px;cursor:pointer"> - </span> </button>
                    <div class="count">${value.quantity}</div>
                    <button onclick="changeQuantity(${key}, ${value.quantity + 1})"><span style="font-size:20px;width:100px;cursor:pointer"> + </span> </button>
                </div>`;

                listCard.appendChild(newDiv);

                let newDiv2 = document.createElement('li');
                newDiv2.innerHTML = `
                <hr style="border: 0; /* Remove default border */
  background-color: rgb(190, 184, 184);
  height: 0.5px; /* Set height of the line */
  margin-left:-20px;
  width:290%;
  ">
                `;
                listCard.appendChild(newDiv2);
        }
    });
    
    
    console.log(Number(ip));
    total.innerText = "$" + (Number(ip) + Number(sp) + Number(totalPrice)).toLocaleString();
    pu.innerText = "$" + (Number(ip) + Number(sp) + Number(totalPrice)).toLocaleString();
    console.log(total);
    
    tpp.value = (Number(ip) + Number(sp) + Number(totalPrice)).toLocaleString();
   
    quantity.innerText = count;
    fp.value = totalPrice.toLocaleString();
    fgh.textContent = totalPrice.toLocaleString();
    gh.textContent =  (Number(ip) + Number(sp) + Number(totalPrice)).toLocaleString();

}
function changeQuantity(key, quantity){
    if(quantity == 0){
        delete listCards[key];
    }else{
        listCards[key].quantity = quantity;
        listCards[key].price = quantity * products[key].price;
    }
    reloadCard();
}