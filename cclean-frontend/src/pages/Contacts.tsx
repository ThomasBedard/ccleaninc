import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translations
import "./Contacts.css";

const Contacts = () => {
  const { translations } = useLanguage(); // ✅ Get translations from context

  return (
    <div className="contacts-container">
      <div className="contacts-header">
        <h1>{translations.contacts?.title || "Contact Us"}</h1>
        <p className="subtitle">
          {translations.contacts?.subtitle || "If you have a question about C CLEAN INC. services, please feel free to contact us."}
        </p>
      </div>
      <div className="contacts-content">
        <div className="contacts-details">
          <h2>{translations.contacts?.companyName || "C CLEAN INC."}</h2>
          <p>{translations.contacts?.address || "3042 RUE MASSON, QUEBEC"}</p>
          <p>{translations.contacts?.phone || "TEL: 514-555-5555"}</p>
          <p>{translations.contacts?.email || "EMAIL: fffffff@gmail.com"}</p>
        </div>
        <div className="contacts-image">
          <img src="/images/telephone.png" alt="Telephone" />
        </div>
      </div>
    </div>
  );
};

export default Contacts;
