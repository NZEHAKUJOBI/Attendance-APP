import React from 'react';
import biometricLogo  from './biometric.png' ;

function BiometricCard() {
  return (
    <div className="card border-success mb-3" style={{ maxWidth: '20rem' }}>
     
      <div className="card-body text-success">
      
        <img src={biometricLogo} alt="Biometric Logo" style={{ width: '300px', marginRight: '100px' }} />
      </div>
     
    </div>
  );
}

export default BiometricCard;
