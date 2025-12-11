# 🏃 Fitness App (JavaFX)

## 📖 Overview
Fitness App is a simple desktop application built with **JavaFX** that helps users track their daily steps, estimate calories burned, and add personal notes.  
All entries are stored in a local CSV file (`steps.csv`) for easy persistence and retrieval.

## 🚀 Features
- Record daily step counts with optional notes  
- Automatic calorie calculation (`steps × 0.04`)  
- Save, update, and delete entries by date  
- Persistent storage in `steps.csv`  
- Lightweight and extendable JavaFX architecture  

## 🛠️ Project Structure
<img width="879" height="881" alt="image" src="https://github.com/user-attachments/assets/4c6ed2e5-d44e-49e5-9b7e-b9c6695126bf" />

## 📂 How It Works
- **StepEntry**:  
  - Stores `date`, `steps`, `calories`, and `note`.  
  - Calories are auto-calculated when steps are set.  

- **StorageManager**:  
  - Saves entries to `steps.csv`.  
  - Supports `save`, `update`, `delete`, and `load` operations.  
  - Ensures the file is created if missing.  

<img width="1772" height="1160" alt="image" src="https://github.com/user-attachments/assets/210e5617-50ef-4097-9d5f-bcd5a30934bf" />

## Video
https://drive.google.com/drive/u/0/folders/1cQssZ55fSJ3te_VbYNMmKUFM57Bxv5EQ


## ▶️ Usage
1. Clone the repository:
   ```bash
   git clone https://github.com/Culevisto/fitness_app-JavaFX.git
   - Open in IntelliJ IDEA (or another Java IDE).
2 - Run the app with Java 17+ and JavaFX SDK installed.
3 - Interact with the app to add, update, or delete step entries.

📊 Example CSV Output
2025-12-11,5000,200.0,"Morning walk"
2025-12-12,8000,320.0,"Evening jog"

🤝 Contributing
Contributions are welcome!
- Fork the repo
- Create a feature branch
- Submit a Pull Request
📜 License
This project is licensed under the MIT License.

---

This README is **directly aligned with your code**: it explains `StepEntry` and `StorageManager`, highlights the calorie formula, and shows how data is stored in `steps.csv`.  

