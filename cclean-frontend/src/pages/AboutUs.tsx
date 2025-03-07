import "./AboutUs.css";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translation hook

const AboutUs = () => {
  const { translations } = useLanguage(); // ✅ Get translations from context

  return (
    <div className="about-us-container">
      <div className="about-us-header">
        <h1>{translations.about_us?.header || "Purpose and Values"}</h1>
        <p className="subtitle">
          {translations.about_us?.subtitle ||
            "Founded in 2020, C CLEAN INC. is dedicated to providing exceptional cleaning services to commercial clients with a strong commitment to integrity, reliability, and customer satisfaction."}
        </p>
      </div>
      <div className="about-us-body">
        <p>
          {translations.about_us?.body ||
            "Our core values emphasize professionalism and a deep respect for our clients and their spaces. We believe in creating clean and healthy environments that enhance the quality of life for everyone we serve. As we expand into residential cleaning contracts, our purpose remains steadfast: to deliver top-notch cleaning solutions that reflect our dedication to excellence and our passion for fostering a cleaner, more sustainable world."}
        </p>
      </div>
    </div>
  );
};

export default AboutUs;
