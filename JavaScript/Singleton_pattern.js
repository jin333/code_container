
var singleton_object = (function(){
    var instance = null;

    let name = "singleton_object";

    function init(){
        return {
            publicMethod : function(){
                console.log("Created!.."+name);
            },
            publicporps : name
        }
    }

    return {
        getInstance : function () {
            if (!instance){
                instance = init();
            }//if
            return instance;
        }
    }
})();

var instance01 = singleton_object.getInstance();
instance01.publicMethod();
console.log('hello?..'+instance01.publicporps);

var instance02 =  singleton_object.getInstance();
instance02.publicMethod();
console.log('hello?..'+instance02.publicporps);

if (instance01 === instance02) {
    console.log('instance01 & instance02 is same!')
} else {
    console.log('instance01 & instance02 is different!')
}