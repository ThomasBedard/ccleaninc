import { useState, useEffect, ReactNode } from "react";
import { Language, Translations, LanguageContext } from "./LanguageContext"; // ✅ Import LanguageContext

export const LanguageProvider = ({ children }: { children: ReactNode }) => {
  const [language, setLanguage] = useState<Language>("en");
  const [translations, setTranslations] = useState<Translations>({});

  // ✅ Function to load translations dynamically
  const loadTranslations = async (lang: Language) => {
    try {
      const response = await fetch(`/locals/${lang}/translation.json`, {
        headers: { "Content-Type": "application/json", Accept: "application/json" },
      });

      if (!response.ok) {
        throw new Error(`Failed to load translation file: ${response.status} ${response.statusText}`);
      }

      const data: Translations = await response.json();

      if (Object.keys(data).length === 0) {
        throw new Error("Translation file is empty.");
      }

      setTranslations(data);
    } catch (error) {
      console.error(`Error loading ${lang} translations:`, error);
    }
  };

  // ✅ Load translations when language changes
  useEffect(() => {
    loadTranslations(language);
  }, [language]);

  const toggleLanguage = () => {
    setLanguage((prev) => (prev === "en" ? "fr" : "en"));
  };

  return (
    <LanguageContext.Provider value={{ language, translations, toggleLanguage }}>
      {children}
    </LanguageContext.Provider>
  );
};
