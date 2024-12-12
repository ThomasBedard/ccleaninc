import React from 'react';
import './Contacts.css';

const Contacts = () => {
  return (
    <div className="contacts-container">
      <div className="contacts-header">
        <h1>Contact Us</h1>
        <p className="subtitle">
          If you have a question about C CLEAN INC. services, please feel free to contact us.
        </p>
      </div>
      <div className="contacts-content">
        <div className="contacts-details">
          <h2>C CLEAN INC.</h2>
          <p>3042 RUE MASSON, QUEBEC</p>
          <p>TEL: 514-555-5555</p>
          <p>EMAIL: fffffff@gmail.com</p>
        </div>
        <div className="contacts-image">
          <img src="/images/telephone.png" alt="Telephone" />
        </div>
      </div>
    </div>
  );
};

export default Contacts;
