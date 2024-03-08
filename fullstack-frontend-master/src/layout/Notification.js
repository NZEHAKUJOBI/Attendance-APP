import React from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";

const CustomToast = ({ closeToast }) => {
  return (
    <div>
      <p>Some Thing Went Wrong!</p>
      <button onClick={closeToast}>Close</button>
    </div>
  );
};

toast.configure();
function Notification() {
  const notify = () => {
    toast("Basic Notification!", {
      position: toast.POSITION.TOP_LEFT
    });
    toast.success("Success Notification!", {
      position: toast.POSITION.TOP_RIGHT,
      autoClose: false
    });
    toast.info("Information Notification!", {
      position: toast.POSITION.BOTTOM_LEFT,
      draggable: true
    });
    toast.warn(<CustomToast />, {
      position: toast.POSITION.BOTTOM_RIGHT,
      autoClose: false,
      draggable: true
    });
    toast.error("Error Notification!", {
      position: toast.POSITION.TOP_CENTER,
      autoClose: 20000
    });
    toast("Basic Notification!", {
      position: toast.POSITION.BOTTOM_CENTER,
      autoClose: false
    });
  };

  return (
    <div className="App">
      <button onClick={notify} className="Notifybutton">
        Show Toasts
      </button>
    </div>
  );
}

export default Notification;
