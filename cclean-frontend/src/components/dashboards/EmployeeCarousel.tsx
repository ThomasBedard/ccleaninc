import "./EmployeeCarousel.css";

const employees = [
  {
    name: "Jane Doe",
    role: "UI & UX Designer",
    imgUrl: "/images/employee-1169923_1920.jpg",
  },
  {
    name: "Alex Smith",
    role: "CEO Expert",
    imgUrl: "/images/man-7450033_1920.jpg",
  },
  {
    name: "Emily New",
    role: "Web Designer",
    imgUrl: "/images/man-7428290_1920.jpg",
  },
  {
    name: "Lisa Boley",
    role: "Marketing Coordinator",
    imgUrl: "/images/portrait-3353699_1920.jpg",
  },
];

const EmployeeCarousel = () => {
  return (
    <div className="carousel-container">
      {employees.map((employee, index) => (
        <div
          key={index}
          style={{
            backgroundImage: `url(${employee.imgUrl})`,
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        >
          <div className="content">
            <h2>{employee.name}</h2>
            <span>{employee.role}</span>
          </div>
        </div>
      ))}
    </div>
  );
};

export default EmployeeCarousel;
