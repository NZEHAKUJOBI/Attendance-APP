// Filename - App.js

import React from "react";

// Importing toastify module
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
toast.configure();

// This is main function
function Toast() {
	const notify = () => {
		toast("Biometric Captured succefuly");
	};
	return (
		<div className="GeeksforGeeks">
			<button onClick={notify}>Click Me!</button>
		</div>
	);
}

export default Toast;
