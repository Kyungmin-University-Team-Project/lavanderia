import React from 'react'
import { FiArrowUp } from 'react-icons/fi'

const FloatingUpButton = () => {
  const scrollToTop = () => {
    window.scrollTo({ top: 0 })
  }

  return (
    <div className="fixed m-auto bottom-6 right-6">
      <button
        onClick={scrollToTop}
        className="bg-gray-800 text-white p-3 rounded-full shadow-lg flex items-center justify-center opacity-70"
      >
        <FiArrowUp className="h-6 w-6" />
      </button>
    </div>
  )
}

export default FloatingUpButton
