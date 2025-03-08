import os
from flask import Flask, request, jsonify
import PyPDF2
import uuid  # To generate unique filenames

app = Flask(__name__)

# Function to extract text from the PDF
def extract_text_from_pdf(pdf_path):
    with open(pdf_path, 'rb') as file:
        reader = PyPDF2.PdfReader(file)
        text = ""
        for page in reader.pages:
            text += page.extract_text()
        return text

# Define the skills for a Full Stack Developer
full_stack_skills = [
    "HTML", "CSS", "JavaScript", "React", "Node.js", "Express",
    "MongoDB", "SQL", "RESTful APIs", "Git", "Python", "Java", 
    "C#", "PHP", "Django", "Flask", "Vue.js", "TypeScript"
]

# Skills for a Data Analyst

data_analyst_skills = [
    "SQL", "Python", "Git", "R", "Excel", "Tableau", "Power BI",
    "Data Mining", "Data Visualization", "Machine Learning"
]

# Skills for a Java Developer
java_developer_skills = [
    "Java", "Spring", "Spring Boot", "Hibernate", "JPA", "Maven", 
    "JUnit", "RESTful APIs", "Microservices", "SQL", "Git", "Docker", 
    "Kubernetes", "Jenkins", "AWS", "Azure", "Multithreading"
]

# Skills for a Python Developer

python_developer_skills = [
    "Python", "Django", "Flask", "Pandas", "NumPy", "SQLAlchemy", 
    "RESTful APIs", "Machine Learning", "TensorFlow", "PyTorch", 
    "SQL", "Git", "Docker", "AWS", "Azure", "FastAPI", "Celery"
]

# Skills for MERN developer

mern_stack_skills = [
    "MongoDB", "Express", "React", "Node.js", "JavaScript", "HTML", 
    "CSS", "RESTful APIs", "Git", "Redux", "Next.js", "TypeScript", 
    "Webpack", "Mongoose", "Jest", "GraphQL", "Docker"
]

# Function to count matching skills in the resume text
def count_matching_skills(resume_text, skills):
    resume_text_lower = resume_text.lower()
    matched_skills = [skill for skill in skills if skill.lower() in resume_text_lower]
    return matched_skills

# Function to calculate the score based on matched skills
def calculate_score(matched_skills, total_skills):
    score = (len(matched_skills) / total_skills) * 100
    return score

# Route to process Full Stack Developer resume
@app.route('/api/ai-process/full-stack', methods=['POST'])
def ai_process_full_stack():
    if 'resume' not in request.files:
        return jsonify({"error": "No resume file provided"}), 400
    
    file = request.files['resume']
    
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    unique_filename = f'tmp/{uuid.uuid4()}.pdf'
    try:
        # Save the file temporarily with a unique name
        file.save(unique_filename)

        # Extract text from the uploaded resume
        resume_text = extract_text_from_pdf(unique_filename)

        # Count matching skills for full stack
        matched_skills = count_matching_skills(resume_text, full_stack_skills)

        # Calculate the score
        score = calculate_score(matched_skills, len(full_stack_skills))

        return jsonify({"aiScore": score, "matchedSkills": matched_skills})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        # Remove the temporary file after processing
        if os.path.exists(unique_filename):
            os.remove(unique_filename)

# Route to process Data Analyst resume
@app.route('/api/ai-process/data-analyst', methods=['POST'])
def ai_process_data_analyst():
    if 'resume' not in request.files:
        return jsonify({"error": "No resume file provided"}), 400
    
    file = request.files['resume']
    
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    unique_filename = f'tmp/{uuid.uuid4()}.pdf'
    try:
        # Save the file temporarily with a unique name
        file.save(unique_filename)

        # Extract text from the uploaded resume
        resume_text = extract_text_from_pdf(unique_filename)

        # Count matching skills for data analyst
        matched_skills = count_matching_skills(resume_text, data_analyst_skills)

        # Calculate the score
        score = calculate_score(matched_skills, len(data_analyst_skills))

        return jsonify({"aiScore": score, "matchedSkills": matched_skills})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        # Remove the temporary file after processing
        if os.path.exists(unique_filename):
            os.remove(unique_filename)
        
@app.route('/api/ai-process/java-developer', methods=['POST'])
def ai_process_java_developer():
    if 'resume' not in request.files:
        return jsonify({"error": "No resume file provided"}), 400
    
    file = request.files['resume']
    
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    unique_filename = f'tmp/{uuid.uuid4()}.pdf'
    try:
        # Save the file temporarily with a unique name
        file.save(unique_filename)

        # Extract text from the uploaded resume
        resume_text = extract_text_from_pdf(unique_filename)

        # Count matching skills for Java Developer
        matched_skills = count_matching_skills(resume_text, java_developer_skills)

        # Calculate the score
        score = calculate_score(matched_skills, len(java_developer_skills))

        return jsonify({"aiScore": score, "matchedSkills": matched_skills})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        # Remove the temporary file after processing
        if os.path.exists(unique_filename):
            os.remove(unique_filename)

# Route to process Python Developer resume
@app.route('/api/ai-process/python-developer', methods=['POST'])
def ai_process_python_developer():
    if 'resume' not in request.files:
        return jsonify({"error": "No resume file provided"}), 400
    
    file = request.files['resume']
    
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    unique_filename = f'tmp/{uuid.uuid4()}.pdf'
    try:
        # Save the file temporarily with a unique name
        file.save(unique_filename)

        # Extract text from the uploaded resume
        resume_text = extract_text_from_pdf(unique_filename)

        # Count matching skills for Python Developer
        matched_skills = count_matching_skills(resume_text, python_developer_skills)

        # Calculate the score
        score = calculate_score(matched_skills, len(python_developer_skills))

        return jsonify({"aiScore": score, "matchedSkills": matched_skills})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        # Remove the temporary file after processing
        if os.path.exists(unique_filename):
            os.remove(unique_filename)


@app.route('/api/ai-process/mern-stack', methods=['POST'])
def ai_process_mern_stack():
    if 'resume' not in request.files:
        return jsonify({"error": "No resume file provided"}), 400
    
    file = request.files['resume']
    
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    unique_filename = f'tmp/{uuid.uuid4()}.pdf'
    try:
        # Save the file temporarily with a unique name
        file.save(unique_filename)

        # Extract text from the uploaded resume
        resume_text = extract_text_from_pdf(unique_filename)

        # Count matching skills for MERN stack
        matched_skills = count_matching_skills(resume_text, mern_stack_skills)

        # Calculate the score
        score = calculate_score(matched_skills, len(mern_stack_skills))

        return jsonify({"aiScore": score, "matchedSkills": matched_skills})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        # Remove the temporary file after processing
        if os.path.exists(unique_filename):
            os.remove(unique_filename)



if __name__ == '__main__':
    # Create a temporary folder to store uploaded resumes
    if not os.path.exists('tmp'):
        os.makedirs('tmp')
    
    app.run(debug=False)