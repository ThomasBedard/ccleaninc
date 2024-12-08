import Navbar from './Navbar';

const Layout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div>
      <Navbar />
      <main className="bg-gray-50 min-h-screen">
        {children}
      </main>
    </div>
  );
};

export default Layout;
