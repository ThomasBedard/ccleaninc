import React from 'react';

const Sidebar = () => {
  return (
    <div className="flex flex-col w-40 h-screen text-black bg-gray-100 rounded-lg">
      {/* Header */}
      <div className="flex items-center w-full px-3 mt-3">
        <svg
          className="w-8 h-8 fill-current text-gray-600"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 20 20"
          fill="currentColor"
        >
          <path d="M11 17a1 1 0 001.447.894l4-2A1 1 0 0017 15V9.236a1 1 0 00-1.447-.894l-4 2a1 1 0 00-.553.894V17zM15.211 6.276a1 1 0 000-1.788l-4.764-2.382a1 1 0 00-.894 0L4.789 4.488a1 1 0 000 1.788l4.764 2.382a1 1 0 00.894 0l4.764-2.382zM4.447 8.342A1 1 0 003 9.236V15a1 1 0 00.553.894l-4 2A1 1 0 009 17v-5.764a1 1 0 00-.553-.894l-4-2z" />
        </svg>
        <span className="ml-2 text-sm font-bold text-black">C CLEAN INC.</span>
      </div>

      {/* Menu Items */}
      <div className="flex flex-col items-center w-full px-2">
        <div className="w-full mt-3 border-t border-gray-300">
          <a
            className="flex items-center w-full h-12 px-3 mt-2 rounded hover:bg-gray-300 hover:text-black text-black"
            href="#"
          >
            <svg
              className="w-6 h-6 stroke-current text-black"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
              />
            </svg>
            <span className="ml-2 text-sm font-medium">Appointments</span>
          </a>
          <a
            className="flex items-center w-full h-12 px-3 mt-2 rounded hover:bg-gray-300 hover:text-black text-black"
            href="#"
          >
            <svg
              className="w-6 h-6 stroke-current text-black"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
            <span className="ml-2 text-sm font-medium">Employees</span>
          </a>
          <a
            className="flex items-center w-full h-12 px-3 mt-2 rounded hover:bg-gray-300 hover:text-black text-black"
            href="#"
          >
            <svg
              className="w-6 h-6 stroke-current text-black"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M16 8v8m-4-5v5m-4-2v2m-2 4h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
              />
            </svg>
            <span className="ml-2 text-sm font-medium">Customers</span>
          </a>
          <a
            className="flex items-center w-full h-12 px-3 mt-2 rounded hover:bg-gray-300 hover:text-black text-black"
            href="#"
          >
            <svg
              className="w-6 h-6 stroke-current text-black"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M8 7v8a2 2 0 002 2h6M8 7V5a2 2 0 012-2h4.586a1 1 0 01.707.293l4.414 4.414a1 1 0 01.293.707V15a2 2 0 01-2 2h-2M8 7H6a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2v-2"
              />
            </svg>
            <span className="ml-2 text-sm font-medium">Estimates</span>
          </a>
          <a
            className="flex items-center w-full h-12 px-3 mt-2 rounded hover:bg-gray-300 hover:text-black text-black"
            href="#"
          >
            <svg
              className="w-6 h-6 stroke-current text-black"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"
              />
            </svg>
            <span className="ml-2 text-sm font-medium">Email</span>
          </a>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;

