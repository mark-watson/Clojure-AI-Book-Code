from transformers import pipeline

qa = pipeline(
    "question-answering",
    #model="NeuML/bert-small-cord19qa",
    model="NeuML/bert-small-cord19-squad2",
    tokenizer="NeuML/bert-small-cord19qa"
)

def answer (query_text,context_text):
  answer = qa({
                "question": query_text,
                "context": context_text
               })
  print(answer)
  return answer
