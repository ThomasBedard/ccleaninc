// Navbar.tsx

const UsersNavbar = () => {
  return (
    <div className="flex justify-between items-center bg-gray-100 p-4 shadow">
      <div className="text-gray-600 font-medium text-lg">Admin Dashboard</div>
      <input
        type="text"
        placeholder="Search"
        className="rounded-full border border-gray-300 px-4 py-2 focus:outline-none"
      />
    </div>
  );
};

export default UsersNavbar;
