import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="bg-gradient-to-b from-[#E4EDF5] to-[#F7F8FC] min-h-screen relative">
      <header className="flex flex-col lg:flex-row justify-between items-center py-20 px-10 lg:px-0">
        {/* Text Content */}
        <div className="max-w-lg z-10 lg:pl-10">
          <h1 className="text-5xl font-bold text-gray-900">C CLEAN inc.</h1>
          <p className="mt-6 text-lg text-gray-600">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
            incididunt ut labore et dolore magna aliqua ut enim ad.
          </p>
          <button
            onClick={() => navigate('/about-us')}
            className="mt-6 px-6 py-3 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-600"
          >
            Learn More
          </button>
        </div>

        {/* Full-Width Image */}
        <div className="absolute inset-y-0 right-0 w-full lg:w-1/2 flex justify-end">
          <img
            src="/images/cleaning-product.png"
            alt="Cleaning Product"
            className="h-full object-contain lg:object-cover"
          />
        </div>
      </header>
    </div>
  );
};

export default Home;