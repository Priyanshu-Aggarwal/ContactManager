console.log("Hello JS");

//const toggleSidebar=()=>
//{
//    if($(".sidebar").is(":visible"))
//    {
//        $(".sidebar").css("display","none");
//          $(".content").css("margin-left","0%");
//    }
//    
//    else
//    {
//        
//        $(".sidebar").css("display","block");
//          $(".content").css("margin-left","20%");
//    }
//};

/* Set the width of the side navigation to 250px */


function openNav() {
  document.getElementById("mySidenav").style.width = "250px";
   $(".content").css("margin-left","20%");
   $(".bar").css("display","none");
};

/* Set the width of the side navigation to 0 */
function closeNav() {
    document.getElementById("mySidenav").style.width = "0px";
     $(".content").css("margin-left","0%");
     $(".bar").css("display","block");
};

const dataFetch=()=>
{
    let query=$("#search").val();
    console.log(query);
    
    if(query=="")
    {
        $(".search-result").hide();
    }
    else
    {
      
        let url=`http://localhost:8080/search/${query}`;
        fetch(url)                          //javascript fetch API to fetch data in json format
                                              // fetch ek promise return krti hai jo uska next then catch krta hai and variabl;e me store    
                                               // krta hai like response and wo response variable ka data next then catch krta hai
                                               
         .then((response)=>{
            return response.json();
        })
        .then((data)=>{
          console.log(data);  
          
          
           let text=`<div class='list-group'>`
          data.forEach((contact)=>{
            text+=`<a href='/user/contact/${contact.cId}' class='list-group-item list-group-action'>${contact.name}</a>`;  
          });
            
           
            text+=`</div>`;
            $(".search-result").html(text);
            
                            $(".search-result").show();
                           
        });
    }
   };
  
   
  