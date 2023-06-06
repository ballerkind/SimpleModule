const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

module.exports = {
	randomString: function(length) {
		let s = "";

		for(let i = 0; i < length; i++) {
			s += chars.charAt(Math.floor(chars.length * Math.random()));
		}

		return s;
	},

	binarySearch: function(array, key, compare) {
		let low = 0;
		let high = array.length - 1;

		while(low <= high) {
			let mid = Math.floor((low + high) / 2);
			let c = compare(array[mid], key);

			if(c < 0) {
				low = mid + 1;
			} else if(c > 0) {
				high = mid - 1;
			} else {
				return mid; // key found
			}
		}

		return -(low + 1); // key not found
	}
};