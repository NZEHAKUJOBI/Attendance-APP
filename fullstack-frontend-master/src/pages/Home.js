import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import BiometricCard from "../layout/BiometricCard";

export default function Home() {
  const [users, setUsers] = useState([]);

  const { id } = useParams();

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    const result = await axios.get("http://localhost:8080/users");
    console.log("the id are{id}");
    setUsers(result.data);
  };

  const deleteUser = async (id) => {
    await axios.delete(`http://localhost:8080/user/${id}`);
    loadUsers();
  };

  const captureUser = async (userId, biometricData, loadUsers) => {
    try {
        // Send a GET request to the biometric endpoint with user ID and biometric data as query parameters
        await axios.get(`http://localhost:8080/fingerprint/${userId}?biometricData=${biometricData}`);
    
        // Refresh the user list after capturing biometrics
        loadUsers();
    
        console.log("Successfully captured biometrics");
    } catch (error) {
        console.error("Error capturing biometrics:", error);
        // Handle error if needed
    }
};

  
  
  
  

  // const captureUser = async (id) => {
  //   try {
  //     // Send a GET request to the biometric endpoint with the user ID
  //     await axios.get(`http://localhost:8080/fingerprint/${id}`);
  //     loadUsers(); // Refresh the user list after capturing biometrics
  //     console.log("the id is this {id}");
  //   } catch (error) {
  //     console.error("Error capturing biometrics:", error);
  //     // Handle error if needed
  //   }
  // };
  
  


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
              {/* <a href="#" class="btn btn-primary" style="margin-right: 10px;">Check-in</a> */}
              <a href="#" class="btn btn-primary">Check-out</a>
              <a href="#" class="btn btn-primary">Check-in</a>
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
                      {/* <Link className="btn btn-outline-primary mx-2" to={`/edituser/${user.id}`}>
                        Edit
                      </Link> */}
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
