import axios from "axios";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import BiometricCard from "../layout/BiometricCard";

export default function AddUser() {
  let navigate = useNavigate();

  const [user, setUser] = useState({
    fullName: "",
    designation : "",
    facility: "",
    phone_number: "",
  });


  const hospitalList = [
    "Abubakar Barde First Referral Hospital",
    "Christian Reformed Church in Nigeria CRCN Hospital Wukari",
    "Federal Medical Centre Jalingo",
    "First Referal Hospital Donga",
    "First Referral Hospital Baissa",
    "First Referral Hospital Ibi",
    "First Referral Hospital Lau",
    "First Referral Hospital Mutum Biyu",
    "First Referral Hospital Serti",
    "First Referral Hospital Sunkani",
    "GECHAAN Lifeline Center",
    "General Hospital Bali",
    "General Hospital Bambur",
    "General Hospital Gembu",
    "General Hospital Warwar",
    "General Hospital Wukari",
    "General Hospital Zing",
    "Government House Clinic",
    "Haruna Tsokwa Memorial General Hospital",
    "Jinya Medical Centre",
    "Kwararrafa Hospital",
    "Mambilla Baptist Hospital",
    "Rapha Hospital",
    "Sancta Maria Clinic Bali",
    "St. Monica’s Health Centre Yakoko",
    "Sun Shine Clinic Baissa",
    "Taraba Specialist Hospital Jalingo",
    "Ummah Clinic",
    "United Methodist Church of Nigeria UMCN Hospital"
  ];
  

  
  const { fullName, designation, facility, phone_number } = user;

  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/user", user);
    navigate("/");
  };


  const [selectedHospital, setSelectedHospital] = useState("");

  const handleSelectChange = (event) => {
    setSelectedHospital(event.target.value);
  };

  return (
    <div className="container">
    <div className="row">
      <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
        <h2 className="text-center m-4">Register User</h2>
        <form onSubmit={(e) => onSubmit(e)}>
          <div className="mb-3">
            <label htmlFor="fullName" className="form-label">
              Full Name
            </label>
            <input
              type={"text"}
              className="form-control"
              placeholder="Full Name"
              name="fullName"
              value={fullName}
              onChange={(e) => onInputChange(e)}
               required
            />
          </div>
            <div className="mb-3">
            <label htmlFor="designation " className="form-label">
              Designation
            </label>
            <select 
              className="form-select" 
              aria-label="Default select example"
              name="designation" 
              value={designation} 
              onChange={(e) => onInputChange(e)}
              required
            >
              <option value="">Select Designation</option>
              <option value="Data Entry Clerk">Data Entry Clerk</option>
              <option value="Case Manager">Case Manager</option>
              <option value="Counselor Tester">Counselor Tester</option>
              <option value="Mentor Mother">Mentor Mother</option>
              <option value="Nurse Tech">Nurse Tech</option>
              <option value="ESM Nurse">ESM Nurse</option>
              <option value="SI Optimizer">SI Optimizer</option>
              <option value="HTS Optimizer">HTS Optimizer</option>
              <option value="Pharmacy Optimizer">Pharmacy Optimizer</option>
            </select>
          </div>


            {/* <div className="mb-3">
              <label htmlFor="Publication_year" className="form-label">
                
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter the publication year"
                name="publication_year"
               2
                onChange={(e) => onInputChange(e)}
              />
            </div> */}

            <div className="mb-3">
              <label htmlFor="facilityName" className="form-label">
                Facility Name
              </label>
              <select 
                className="form-select"  
                id="facility"
                name="facility"
                value={facility}
                onChange={(e) => onInputChange(e)}
                required
              >
                <option value="" required>Select a Hospital</option>
                {hospitalList.map((hospital, index) => (
                  <option key={index} value={hospital}>
                    {hospital}
                  </option>
                ))}
              </select>
            </div>



            <div className="mb-3">
              <label htmlFor="phone_number" className="form-label">
                 Phone Number
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter the Phone Number"
                name="phone_number"
                value={phone_number}
                onChange={(e) => onInputChange(e)}
                required
              />
            </div>
            
            <div className="mb-3 d-flex justify-content-center">
         
          <button type="submit" className="btn btn-outline-primary me-2">
            Submit
          </button>
          <Link className="btn btn-outline-danger" to="/">
            Cancel
          </Link>
        </div >
       
          </form>
        </div>
      </div>
    </div>
  );
}
