// set global variables
var likeThresh = 0.7;
var commentThresh = 1;
var garment = [0,0,0,0,0,0,0,0,0,0];

var profiles = {
				"friendsRec" : [],
				"nonFriendsRec":[]
			};



function setProfiles(profileCount,max,min){


	for (var i = 0; i < profileCount; i++) {
	
	// create random wether friend or not
	var isFriend = Math.random() >= 0.5;
	

	var marks = 0.3;
	var weight = 0.3;
	var likeCount = 0;
	var commCount = 0;
	var age = Math.floor(Math.random() * (50 - 12 + 1)) + 12;
	var gender;

	if(Math.random() >= 0.5){
		gender = "male";
	}else{
		gender = "female";
	}

	if(isFriend == true){
	// create random of likes 	
	likeCount = Math.floor(Math.random() * (max - min + 1)) + min;

	// create random of comments
	commCount = Math.floor(Math.random() * (max - min + 1)) + min;

	// calculate the weight
	var marks = calculateWeight(likeCount,commCount,likeThresh,commentThresh);

	}



	var recListArr = generateUniqueRating(); 

	// create the profiles
	var profile = {
		id : i,
		"isFriend" : isFriend,
		"marks" : marks,
		"weight" : weight,
		"likes" : likeCount,
		"comments" : commCount,
		"age" : age,
		"gender" : gender,
		"recList" : recListArr
	}


	if(isFriend == true){
		profiles["friendsRec"].push(profile);
	}else{
		profiles["nonFriendsRec"].push(profile);
	}

	

	};

	return profiles;
}


function generateUniqueRating(){
	var arr = []
	while(arr.length < 10){
	  var randomnumber=Math.ceil(Math.random()*10)
	  var found=false;
	  for(var i=0;i<arr.length;i++){
		if(arr[i]==randomnumber){found=true;break}
	  }
	  if(!found)arr[arr.length]=randomnumber;
	}
	return arr;
}

var sort_by;
// sort the friends based on comments and likes
(function() {
    // utility functions
    var default_cmp = function(a, b) {
            if (a == b) return 0;
            return a < b ? -1 : 1;
        },
        getCmpFunc = function(primer, reverse) {
            var dfc = default_cmp, // closer in scope
                cmp = default_cmp;
            if (primer) {
                cmp = function(a, b) {
                    return dfc(primer(a), primer(b));
                };
            }
            if (reverse) {
                return function(a, b) {
                    return -1 * cmp(a, b);
                };
            }
            return cmp;
        };

    // actual implementation
    sort_by = function() {
        var fields = [],
            n_fields = arguments.length,
            field, name, reverse, cmp;

        // preprocess sorting options
        for (var i = 0; i < n_fields; i++) {
            field = arguments[i];
            if (typeof field === 'string') {
                name = field;
                cmp = default_cmp;
            }
            else {
                name = field.name;
                cmp = getCmpFunc(field.primer, field.reverse);
            }
            fields.push({
                name: name,
                cmp: cmp
            });
        }

        // final comparison function
        return function(A, B) {
            var a, b, name, result;
            for (var i = 0; i < n_fields; i++) {
                result = 0;
                field = fields[i];
                name = field.name;

                result = field.cmp(A[name], B[name]);
                if (result !== 0) break;
            }
            return result;
        }
    }
}());

// calculate the weight
function calculateWeight(likes,comments,likeThresh,commentThresh){
	var likePoints = likes * likeThresh;
	var commPoints = comments * commentThresh;
	var totPoints = likePoints + commPoints;
	return totPoints;
}


function calculateRecomendation(data){

	// sort the array first by comments and then by likes
	data["friendsRec"].sort(sort_by('comments', {name:'likes', primer: parseInt, reverse: false}));


	var maxLength = data.friendsRec.length;
	var maxMarks = data["friendsRec"][maxLength-1]["marks"];
	var minMarks = data["friendsRec"][0]["marks"];

	var friendMax = 0.7;
	var friendMin = 0.4;

	var friendDiff = friendMax - friendMin;
	var dataDiff = maxMarks - minMarks;

	for (var i = 0; i < data["friendsRec"].length; i++) {

		var weight = (friendDiff/dataDiff) * data["friendsRec"][i]['marks'] + friendMin;
		weight = weight.toFixed(2);
		data["friendsRec"][i]['weight'] = weight;

		var user = data["friendsRec"][i];
		
		for (var j = 0; j < user["recList"].length; j++) {
			var rating = user["recList"][j];

			var marks = rating * weight;
			garment[j] = garment[j] + marks;

		};
	};


	for (var i = 0; i < data["nonFriendsRec"].length; i++) {

		var user = data["nonFriendsRec"][i];
		var weight = user.weight;

		for (var j = 0; j < user["recList"].length; j++) {
			var rating = user["recList"][j];

			var marks = rating * weight;
			garment[j] = garment[j] + marks;

		};
	};

}

function getHighestRecommendation(garment){
	var testGarment = garment.slice();
	testGarment.sort();
	var i;

	for (i = 0; i < garment.length; i++) {
		if (garment[i] ==  testGarment[testGarment.length-1]) {
			break;
		};
	};

	return i;
}
