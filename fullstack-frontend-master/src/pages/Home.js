import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import BiometricCard from "../layout/BiometricCard";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function Home() {
  const [users, setUsers] = useState([]);
  const { id } = useParams();

  useEffect(() => {
    loadUsers();
  }, []);

  


  const loadUsers = async () => {
    try {
      const result = await axios.get("http://localhost:8080/users");
      setUsers(result.data);
    } catch (error) {
      console.error("Error loading users:", error);
    }
  };

  // const deleteUser = async (id) => {
  //   try {
  //     await axios.delete(`http://localhost:8080/user/${id}`);
  //     loadUsers();
      
  //   } catch (error) {
  //     console.error("Error deleting user:", error);
  //   }
  // };

  const deleteUser = async (id) => {
    try {
      // Send an HTTP DELETE request to the specified URL
      await axios.delete(`http://localhost:8080/user/${id}`);
  
      // Assuming `loadUsers()` is a function that reloads user data
      loadUsers();
  
      // Check if the response indicates success
      // (You might want to handle this differently based on your application logic)
      toast.success("Deleted successfully");
    } catch (error) {
      // Handle errors (e.g., network issues, server errors)
      console.error("Error deleting user:", error);
    }
  };

  // const captureUser = async (userId) => {
  //   try {
  //     const response = await axios.post(`http://localhost:8080/fingerprint/${userId}`);
  //     if (response.data.success) {
  //       toast.success(" Biometric Captured succefully");
  //       console.log("Successfully captured biometrics");
  //     }
  //   } catch (error) {
  //     console.error("Error capturing biometrics:", error);
  //   }
  // };
  const captureUser = async (userId) => {
    try {
      // Check if user ID already exists in the table
      const userExists = users.some(user => user.id === userId);
      if (userExists) {
        toast.warning("Biometric already captured for this user");
        return; // Exit function early
      }
  
      // If user ID doesn't exist, proceed with capturing biometrics
      const response = await axios.post(`http://localhost:8080/fingerprint/${userId}`);
      if (response.data.success) {
        toast.success("Biometric Captured successfully");
        console.log("Successfully captured biometrics");
      }
    } catch (error) {
      console.error("Error capturing biometrics:", error);
    }
  };
  

  return (
    <div className="container">
      <div className="d-flex flex-column align-items-center" style={{ minHeight: "80vh" }}>
        <div className="card-container" style={{ width: "1800px", maxWidth: "1800px" }}>
          <div className="card p-4 mb-4">
            <h5 className="card-header">STATUS</h5>
            <div className="card-body">
              <div className="mb-3 d-flex justify-content-left">
                <BiometricCard/>
              </div>
              <div>
                <a href="#" className="btn btn-primary">Check-out</a>
                <a href="#" className="btn btn-primary">Check-in</a>
              </div>
            </div>
          </div>

          <div className="card p-4">
            <h2 className="text-center mb-4">Staff List</h2>
            <table className="table border shadow">
              <thead>
                <tr>
                  <th scope="col">S.N</th>
                  <th scope="col">FULL NAME</th>
                  <th scope="col">Designation</th>
                  <th scope="col">Facility</th>
                  <th scope="col">Phone Number</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user, index) => (
                  <tr key={index}>
                    <th scope="row">{index + 1}</th>
                    <td>{user.fullName}</td>
                    <td>{user.designation}</td>
                    <td>{user.facility}</td>
                    <td>{user.phone_number}</td>
                    <td>
                      <Link className="btn btn-primary mx-2" to={`/viewuser/${user.id}`}>
                        View
                      </Link>
                      <button className="btn btn-danger mx-2" onClick={() => deleteUser(user.id)}>
                        Delete
                      </button>
                      <button className="btn btn-danger mx-2" onClick={() => captureUser(user.id)}>
                        Capture Biometrics
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
